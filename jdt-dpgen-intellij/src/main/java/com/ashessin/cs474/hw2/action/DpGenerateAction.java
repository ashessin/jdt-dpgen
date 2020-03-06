package com.ashessin.cs474.hw2.action;

import com.ashessin.cs474.hw1.Main;
import com.ashessin.cs474.hw2.model.DpGenerate;
import com.ashessin.cs474.hw2.ui.DpGenerateDialogWrapper;
import com.intellij.lang.java.JavaLanguage;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import static com.intellij.psi.impl.file.JavaDirectoryServiceImpl.getInstance;

/**
 * The custom Design Pattern generation plugin action class.
 */
public class DpGenerateAction extends AnAction {

	private static final Logger log = Logger.getInstance(DpGenerateAction.class);

	private PsiDirectory selectedDir;

	/**
	 * Updates the state of the action. Default implementation does nothing.
	 * Override this method to provide the ability to dynamically change action's
	 * state and(or) presentation depending on the context (For example
	 * when your action state depends on the selection you can check for
	 * selection and change the state accordingly).<p></p>
	 * <p>
	 * This method can be called frequently, and on UI thread.
	 * This means that this method is supposed to work really fast,
	 * no real work should be done at this phase. For example, checking selection in a tree or a list,
	 * is considered valid, but working with a file system or PSI (especially resolve) is not.
	 * If you cannot determine the state of the action fast enough,
	 * you should do it in the {@link #actionPerformed(AnActionEvent)} method and notify
	 * the user that action cannot be executed if it's the case.<p></p>
	 * <p>
	 * If the action is added to a toolbar, its "update" can be called twice a second, but only if there was
	 * any user activity or a focus transfer. If your action's availability is changed
	 * in absence of any of these events, please call {@code ActivityTracker.getInstance().inc()} to notify
	 * action subsystem to update all toolbar actions when your subsystem's determines that its actions' visibility
	 * might be affected.
	 *
	 * @param event Carries information on the invocation place and data available
	 */
	@Override
	public void update(@NotNull AnActionEvent event) {
		log.info(" Call to DpGenerateAction.update.");
		PsiElement psiElement = CommonDataKeys.PSI_ELEMENT.getData(event.getDataContext());
		log.debug("Selected Element: ", psiElement);

		if (psiElement instanceof PsiDirectory) {
			selectedDir = (PsiDirectory) psiElement;
		} else if (psiElement instanceof PsiClass) {
			selectedDir = psiElement.getContainingFile().getContainingDirectory();
		}

		if (getInstance().getPackageInSources(selectedDir) == null) {
			log.debug("Hiding: ", this.getClass().getSimpleName());
			event.getPresentation().setEnabledAndVisible(false);
		}
	}

	/**
	 * Implement this method to provide your action handler.
	 *
	 * @param event Carries information on the invocation place
	 */
	@Override
	public void actionPerformed(@NotNull AnActionEvent event) {
		log.info(" Call to DpGenerateAction.actionPerformed.");
		Project project = event.getProject();
		DpGenerateDialogWrapper dpGenerateDialogWrapper = new DpGenerateDialogWrapper(project);
		dpGenerateDialogWrapper.show();

		if (dpGenerateDialogWrapper.isOK()) {
			String[] command = DpGenerate.INSTANCE.getCommand();
			String cmd = String.join(" ", command);
			log.debug("Executing: ", cmd);

			try {
				Main cli = new Main();
				cli.processResultMap(cli, command).forEach((s, bytesList) -> bytesList.parallelStream().forEach(bytes -> {
					String fileName = s.substring(s.lastIndexOf('.') + 1).trim() + ".java";
					PsiFile file = PsiFileFactory.getInstance(Objects.requireNonNull(project))
							.createFileFromText(fileName, JavaLanguage.INSTANCE, new String(bytes));

					WriteCommandAction.runWriteCommandAction(project, () -> {
						selectedDir.add(file);
					});
					new OpenFileDescriptor(project, Objects.requireNonNull(selectedDir.findFile(fileName))
							.getVirtualFile()).navigate(true);
				}));

				String msg = "Generated files are in `" + selectedDir.toString().substring(13) + "` directory.";
				Notifier.notify(project, "Success!", msg, NotificationType.INFORMATION);
			} catch (IncorrectOperationException e) {
				Notifier.notify(project, "Incorrect operation!", e.getMessage(), NotificationType.ERROR);
			} catch (Exception e) {
				// very lazy exception handling
				String msg = "Could not execute `" + cmd + "` command.\n" +
							 "Caused by " + e.toString() + " exception. See plugin logs for details.";
				Notifier.notify(project, "An error occurred!", msg, NotificationType.ERROR);
			}
		}
	}

	private static class Notifier {

		private static final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("jdt-dpgen-intellij",
				NotificationDisplayType.STICKY_BALLOON, true);

		public static void notify(Project project, String subtitle, String content,
								  NotificationType notificationType) {
			final Notification notification = NOTIFICATION_GROUP.createNotification("DpGenerate Plugin", subtitle,
					content, notificationType);
			Notifications.Bus.notify(notification, project);
		}
	}
}
