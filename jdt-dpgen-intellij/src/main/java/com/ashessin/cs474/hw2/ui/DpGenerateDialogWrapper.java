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
		List<ValidationInfo> validationInfos = new ArrayList<>(1);
		Set<String> allDistinctIdentifiers = new HashSet<>(1);

		dpGenerateForm.getParameters().forEach((label, textField) -> {
			if (textField.getText().trim().isEmpty()) {
				// all fields are mandatory and can not be empty
				validationInfos.add(new ValidationInfo("This field can not be empty!", textField));
			} else if (Arrays.stream(skipValidate).noneMatch(s -> label.getText().contains(s))) {
				String[] identifiersInField = textField.getText().split("[,;]");
				Set<String> disctinctIdentifiersInField = new HashSet<>(Arrays.asList(identifiersInField));

				checkNameValidity(validationInfos, textField, disctinctIdentifiersInField);

				checkForDuplicate(validationInfos, allDistinctIdentifiers, textField, identifiersInField,
						disctinctIdentifiersInField);
			}
		});
		return validationInfos;
	}

	private void checkNameValidity(List<ValidationInfo> validateInfos, JTextField textField,
								   Set<String> disctinctIdentifiersInField) {
		// name should not be a keyword and be syntactically valid identifier in java
		if (disctinctIdentifiersInField.stream().anyMatch(SourceVersion::isKeyword) ||
			!disctinctIdentifiersInField.stream().allMatch(SourceVersion::isIdentifier)) {
			validateInfos.add(new ValidationInfo("Please use valid identifiers!", textField));
		}
	}

	private void checkForDuplicate(List<ValidationInfo> validateInfos, Set<String> allDistinctIdentifiers,
								   JTextField textField, String[] identifiersInField,
								   Set<String> disctinctIdentifiersInField) {
		// name clash within the same text field
		if (identifiersInField.length != disctinctIdentifiersInField.size()) {
			validateInfos.add(new ValidationInfo("Please use unique names!", textField));
		}
		// name clash across two different text fields
		disctinctIdentifiersInField.forEach(s -> {
			if (!allDistinctIdentifiers.add(s + ".java")) {
				validateInfos.add(new ValidationInfo("Please use unique names!", textField));
			}
		});
		// name clash between text field and reference types already in current package/subpackage
		Arrays.stream(DpGenerate.INSTANCE.getPackageDirectory().getFiles()).forEach(psiFile -> {
			if (psiFile instanceof PsiJavaFile &&
				allDistinctIdentifiers.contains(psiFile.getName())) {
				String msg = "File named " + psiFile.getName() + " already exists in selected package.";
				validateInfos.add(new ValidationInfo(msg, textField));
			}
		});
	}
}
