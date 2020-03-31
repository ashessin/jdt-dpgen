package com.ashessin.cs474.hw2.ui;

import com.ashessin.cs474.hw2.model.DpGenerate;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ashessin.cs474.hw1.Main.getDesignPatterns;

public class DpGenerateForm {

	private static final Logger log = Logger.getInstance(DpGenerateForm.class);
	private static Map<String, Map<String, String>> designPatterns = getDesignPatterns();

	private DpGenerate dpGenerate;
	private Map<JLabel, JTextField> parameters;
	private JPanel mainPanel;
	private JRadioButton selectDpRadioButton;
	private JComboBox<String> selectDpComboBox;
	private JPanel fieldsPanel;
	private JRadioButton useCommandRadioButton;
	private JTextField useCommandTextField;
	private ButtonGroup radioButtonGroup;

	public DpGenerateForm(DpGenerate dpGenerate, DpGenerateDialogWrapper dpGenerateDialogWrapper) {
		log.debug("Populating Design Pattern Generator dialog.");
		this.dpGenerate = dpGenerate;

		// on select dropdown radio button action
		selectDpRadioButton.addActionListener(actionEvent -> {
			// enable 'OK' button
			if (dpGenerateDialogWrapper != null)
				dpGenerateDialogWrapper.setOKActionEnabled(true);
			// disable command
			useCommandRadioButton.setSelected(false);
			useCommandTextField.setEnabled(false);
			// enable dropdown
			selectDpComboBox.setEnabled(true);
			selectDpComboBox.enableInputMethods(true);
			// populate label and fields for dropdown selection
			updateFieldsPanel();
			parseTextFieldToCommand(DpGenerationMethod.SELECT_DP);
		});

		selectDpComboBox.addActionListener(actionEvent -> {
			updateFieldsPanel();
			parseTextFieldToCommand(DpGenerationMethod.SELECT_DP);
		});

		// on select command radio button action
		useCommandRadioButton.addActionListener(actionEvent -> {
			// enable 'OK' button
			if (dpGenerateDialogWrapper != null)
				dpGenerateDialogWrapper.setOKActionEnabled(true);
			// disable dropdown
			selectDpRadioButton.setSelected(false);
			selectDpComboBox.setEnabled(false);
			// enable command
			useCommandTextField.setEnabled(true);
			useCommandTextField.enableInputMethods(true);
			// get pre-filled command text within command field
			clearFieldsPanel();
			parseTextFieldToCommand(DpGenerationMethod.USE_COMMAND);
		});

		useCommandTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent documentEvent) {
				parseTextFieldToCommand(DpGenerationMethod.USE_COMMAND);
			}

			@Override
			public void removeUpdate(DocumentEvent documentEvent) {
				parseTextFieldToCommand(DpGenerationMethod.USE_COMMAND);
			}

			@Override
			public void changedUpdate(DocumentEvent documentEvent) {
				parseTextFieldToCommand(DpGenerationMethod.USE_COMMAND);
			}
		});
	}

	public static Map<String, Map<String, String>> retriveDesignPatterns() {
		return designPatterns;
	}

	public Map<JLabel, JTextField> getParameters() {
		return parameters;
	}

	public JRadioButton retriveSelectDpRadioButton() {
		return selectDpRadioButton;
	}

	public JComboBox<String> retriveSelectDpComboBox() {
		return selectDpComboBox;
	}

	public JPanel retriveFieldsPanel() {
		return fieldsPanel;
	}

	public JRadioButton retriveUseCommandRadioButton() {
		return useCommandRadioButton;
	}

	public JTextField retriveUseCommandTextField() {
		return useCommandTextField;
	}

	private void parseTextFieldToCommand(DpGenerationMethod method) {
		if (method == DpGenerationMethod.SELECT_DP) {
			String[] command = (Objects.requireNonNull(selectDpComboBox.getSelectedItem()).toString()
										.replace(" ", "")
										.toLowerCase() + " " + parameters.values().stream()
										.map(JTextComponent::getText)
										.collect(Collectors.joining(" ")))
					.split(" ");
			dpGenerate.setCommand(command);
		} else {
			dpGenerate.setCommand(useCommandTextField.getText().split(" "));
		}
		log.debug("Updated command to: ", String.join(" ", dpGenerate.getCommand()));
	}

	public JComponent getContent() {
		return mainPanel;
	}

	@SuppressWarnings("unused")
	private void createUIComponents() {
		selectDpComboBox = new ComboBox<>();
		designPatterns.keySet().stream().sorted().forEach(s -> selectDpComboBox.addItem(s));
	}

	private void clearFieldsPanel() {
		fieldsPanel.removeAll();
		fieldsPanel.updateUI();
	}

	private void updateFieldsPanel() {
		Map<String, String> activeSelection = designPatterns
				.get(Objects.requireNonNull(selectDpComboBox.getSelectedItem()).toString());

		// remove fields panel from the main panel
		mainPanel.remove(fieldsPanel);
		mainPanel.updateUI();

		// Update fields panel with new components
		clearFieldsPanel();
		fieldsPanel.setLayout(new GridLayoutManager(activeSelection.size(), 2, JBUI.emptyInsets(), -1, -1));
		parameters = new LinkedHashMap<>(activeSelection.size());
		int row = 0;
		for (Map.Entry<String, String> entry : activeSelection.entrySet()) {
			String labelText = entry.getKey();
			String fieldText = entry.getValue();

			final JLabel label = new JLabel();
			final JTextField textField = new JTextField();

			label.setLabelFor(textField);
			label.setText(labelText + ":");
			fieldsPanel.add(label, new GridConstraints(row, 0, 1, 1, GridConstraints.ANCHOR_WEST,
					GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
					null, new Dimension(200, -1), null, 0, false));
			textField.setText(fieldText);

			parameters.put(label, textField);
			textField.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent documentEvent) {
					parseTextFieldToCommand(DpGenerationMethod.SELECT_DP);
				}

				@Override
				public void removeUpdate(DocumentEvent documentEvent) {
					parseTextFieldToCommand(DpGenerationMethod.SELECT_DP);
				}

				@Override
				public void changedUpdate(DocumentEvent documentEvent) {
					parseTextFieldToCommand(DpGenerationMethod.SELECT_DP);
				}
			});

			fieldsPanel.add(textField, new GridConstraints(row, 1, 1, 1, GridConstraints.ANCHOR_EAST,
					GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
					GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(250, -1), null, 0, false));

			row++;
		}

		// add fields panel to the main panel
		fieldsPanel.setVisible(true);
		fieldsPanel.updateUI();
		mainPanel.add(fieldsPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER,
				GridConstraints.FILL_BOTH,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
				GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
				null, null, null, 3, false));
		mainPanel.updateUI();
	}

	private enum DpGenerationMethod {
		SELECT_DP, USE_COMMAND
	}
}
