package com.ashessin.cs474.hw2.ui;

import com.ashessin.cs474.hw2.model.DpGenerate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.Objects;

class DpGenerateFormTest {

	private static final Logger log = LoggerFactory.getLogger(DpGenerateFormTest.class);
	private static final JFrame frame = new JFrame("DpGenerateFormTest Frame");
	private DpGenerate dpGenerate;
	private DpGenerateForm dpGenerateForm;

	@BeforeEach
	void setUp() {
		dpGenerate = DpGenerate.INSTANCE;
		dpGenerateForm = new DpGenerateForm(dpGenerate);
		frame.setContentPane(dpGenerateForm.getContent());
		frame.pack();
	}

	@Test
	@DisplayName("Initial gui is as per expectation.")
	void getContent() {
		log.info("Testing Initial unmodified GUI components...");

		// check item count in combobox
		Assertions.assertEquals(dpGenerateForm.retriveSelectDpComboBox().getItemCount(), DpGenerateForm
				.retriveDesignPatterns().size());

		// check default selection in combobox
		Assertions.assertTrue(Objects.requireNonNull(dpGenerateForm.retriveSelectDpComboBox().getSelectedItem()).toString()
				.equalsIgnoreCase("Abstract Factory"));

		// check default command in use command text field
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandTextField().getText()
				.equalsIgnoreCase("singleton builder abstractfactory"));

		// check fields panel is initially empty
		Assertions.assertEquals(0, dpGenerateForm.retriveFieldsPanel().getComponents().length);

		// check all radio button unselected and corresponding component disabled
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isEnabled());
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpComboBox().isEnabled());
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandRadioButton().isEnabled());
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandTextField().isEnabled());
	}

	@Test
	void retriveDesignPatterns() {
		Assertions.assertTrue(DpGenerateForm.retriveDesignPatterns().size() >= 23);
	}

	@Test
	@DisplayName("SelectDpRadioButton and related listeners.")
	void retriveSelectDpRadioButton() {
		log.info("Testing SelectDpRadioButton and related listeners...");

		// selectDpRadioButton select
		Assertions.assertFalse((dpGenerateForm.retriveSelectDpRadioButton().isSelected()));
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpComboBox().isEnabled());

		// selectDpRadioButton un-select
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		dpGenerateForm.retriveUseCommandRadioButton().doClick(); // other button in group
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpComboBox().isEnabled());
	}

	@Test
	@DisplayName("UseCommandRadioButton and related listeners.")
	void retriveUseCommandRadioButton() {
		log.info("Testing UseCommandRadioButton and related listeners...");

		// useCommandRadioButton select
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		dpGenerateForm.retriveUseCommandRadioButton().doClick();
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandTextField().isEnabled());

		// useCommandRadioButton un-select
		Assertions.assertTrue(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		dpGenerateForm.retriveSelectDpRadioButton().doClick(); // other button in group
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandRadioButton().isSelected());
		Assertions.assertFalse(dpGenerateForm.retriveUseCommandTextField().isEnabled());
	}

	@Test
	@DisplayName("SelectDpComboBox and related listeners.")
	void retriveSelectDpComboBox() {
		log.info("Testing SelectDpComboBox and related listeners...");
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isEnabled());
		Assertions.assertFalse(dpGenerateForm.retriveSelectDpComboBox().isEnabled());

		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpRadioButton().isSelected());
		Assertions.assertTrue(dpGenerateForm.retriveSelectDpComboBox().isEnabled());

		String previousCommand = "";
		for (String s : DpGenerateForm.retriveDesignPatterns().keySet()) {
			dpGenerateForm.retriveSelectDpComboBox().setSelectedItem(s);
			log.info("combo box select: {}", s);
			String newCommand = String.join(" ", dpGenerate.getCommand());
			Assertions.assertNotNull(newCommand);
			log.info("new command: {}", newCommand);
			Assertions.assertFalse(newCommand.equalsIgnoreCase(previousCommand));
			previousCommand = newCommand;
		}
	}
}