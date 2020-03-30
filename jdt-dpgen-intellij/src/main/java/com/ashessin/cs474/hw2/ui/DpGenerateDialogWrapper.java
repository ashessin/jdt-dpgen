package com.ashessin.cs474.hw2.ui;

import com.ashessin.cs474.hw2.model.DpGenerate;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.psi.PsiJavaFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.SourceVersion;
import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Dialog wrapper for containing {@link DpGenerateForm}.
 */
public class DpGenerateDialogWrapper extends DialogWrapper {

	private static final Logger log = Logger.getInstance(DpGenerateDialogWrapper.class);

	private DpGenerateForm dpGenerateForm;

	/**
	 * Creates modal {@code DialogWrapper} that can be parent for other windows.
	 * The currently active window will be the dialog's parent.
	 *
	 * @param project parent window for the dialog will be calculated based on focused window for the
	 *                specified {@code project}. This parameter can be {@code null}. In this case parent window
	 *                will be suggested based on current focused window.
	 *
	 * @throws IllegalStateException if the dialog is invoked not on the event dispatch thread
	 * @see DialogWrapper#DialogWrapper(Project, boolean)
	 */
	public DpGenerateDialogWrapper(@Nullable Project project) {
		super(project);
		log.info("Showing Design Pattern Generator dialog.");
		dpGenerateForm = new DpGenerateForm(DpGenerate.INSTANCE);
		init();
		setTitle("Design Pattern Generator");
		log.info("Design Pattern Generator dialog is ready.");
	}

	/**
	 * Factory method. It creates panel with dialog options. Options panel is located at the
	 * center of the dialog's content pane. The implementation can return {@code null}
	 * value. In this case there will be no options panel.
	 */
	@Nullable
	@Override
	protected JComponent createCenterPanel() {
		return dpGenerateForm.getContent();
	}

	/**
	 * Validates user input and returns {@code List<ValidationInfo>}.
	 * If everything is fine the returned list is empty otherwise
	 * the list contains all invalid fields with error messages.
	 * This method should preferably be used when validating forms with multiply
	 * fields that require validation.
	 *
	 * @return {@code List<ValidationInfo>} of invalid fields. List
	 * is empty if no errors found.
	 */
	@NotNull
	@Override
	protected List<ValidationInfo> doValidateAll() {
		// only target class/interface name checking
		String[] skipValidate = {"PROPERTIES", "FIELD", "METHOD"};

		List<ValidationInfo> validateInfos = new ArrayList<>(1);
		Set<String> allIdentifierNames = new HashSet<>(1);
		// TODO: Simplify
		dpGenerateForm.getParameters().forEach((label, textField) -> {
			if (Arrays.stream(skipValidate).noneMatch(s -> label.getText().contains(s))) {
				if (textField.getText().trim().isEmpty()) {
					validateInfos.add(new ValidationInfo("This field can not be empty!", textField));
				} else {
					String[] identifierNames = textField.getText().split("[,;]");
					List<String> disctinctIdentifierNames = Arrays.stream(identifierNames)
							.distinct()
							.collect(Collectors.toList());

					if (disctinctIdentifierNames.stream().anyMatch(SourceVersion::isKeyword) ||
						!disctinctIdentifierNames.stream().allMatch(SourceVersion::isIdentifier)) {
						validateInfos.add(new ValidationInfo("Please use valid identifiers!", textField));
					}

					if (identifierNames.length != disctinctIdentifierNames.size()) {
						validateInfos.add(new ValidationInfo("Please use unique names!", textField));
					}
					disctinctIdentifierNames.forEach(s -> {
						if (!allIdentifierNames.add(s + ".java")) {
							validateInfos.add(new ValidationInfo("Please use unique names!", textField));
						}
					});

					Arrays.stream(DpGenerate.INSTANCE.getPackageDirectory().getFiles()).forEach(psiFile -> {
						if (psiFile instanceof PsiJavaFile &&
							allIdentifierNames.contains(psiFile.getName())) {
							String msg = "File named " + psiFile.getName() + " already exists in selected package.";
							validateInfos.add(new ValidationInfo(msg, textField));
						}
					});
				}
			}
		});
		return validateInfos;
	}
}
