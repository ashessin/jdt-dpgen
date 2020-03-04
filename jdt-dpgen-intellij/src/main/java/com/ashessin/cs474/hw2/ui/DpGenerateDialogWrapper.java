package com.ashessin.cs474.hw2.ui;

import com.ashessin.cs474.hw2.model.DpGenerate;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

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
}
