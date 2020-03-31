package com.ashessin.cs474.hw2.ui;

import com.ashessin.cs474.hw2.model.DpGenerate;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiDirectory;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import org.junit.jupiter.api.Assertions;

import javax.swing.*;

public class DpGenerateDialogWrapperTest extends LightJavaCodeInsightFixtureTestCase {

	private DpGenerateDialogWrapper dpGenerateDialogWrapper;
	private DpGenerateForm dpGenerateForm;

	@Override
	protected String getTestDataPath() {
		return "src/test/testData";
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		myFixture.copyDirectoryToProject("com", "");

		PsiClass helloWorld = myFixture.findClass("com.gof.HelloWorld");
		assertNotNull(helloWorld);

		PsiDirectory selectedDir = helloWorld.getContainingFile().getContainingDirectory();
		assertNotNull(selectedDir);
		assertEquals(selectedDir.getName(), "gof");

		DpGenerate.INSTANCE.setPackageDirectory(selectedDir);
	}

	public void testInitalizeState() throws InterruptedException {
		dpGenerateDialogWrapper = new DpGenerateDialogWrapper(getProject());
		Assertions.assertFalse(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm = dpGenerateDialogWrapper.getDpGenerateForm();
		Assertions.assertNotNull(dpGenerateForm);
	}

	public void testEmptyUseCommandField() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveUseCommandRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.retriveUseCommandTextField().setText(null);
		Assertions.assertNotEquals(0, dpGenerateDialogWrapper.doValidateAll().size());
		Assertions.assertEquals("Command can not be empty!", dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}

	public void testEmptyDpSelectField() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		JTextField jTextField = (JTextField) dpGenerateForm.getParameters().values().toArray()[0];
		jTextField.setText(null);
		Assertions.assertNotEquals(0, dpGenerateDialogWrapper.doValidateAll().size());
		Assertions.assertEquals("This field can not be empty!",
				dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}

	public void testEmptyDpSelectFields() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.getParameters().values().forEach(jTextField -> jTextField.setText(null));
		Assertions.assertEquals(0, dpGenerateDialogWrapper.doValidateAll().stream()
				.filter(validationInfo -> !validationInfo.message
						.equalsIgnoreCase("This field can not be empty!"))
				.count());
	}

	public void testDuplicatesAcrossTwoDpSelectFields() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.retriveSelectDpComboBox().setSelectedItem("Builder");
		JTextField[] builderDpSelectFields = dpGenerateForm.getParameters().values().toArray(new JTextField[0]);
		Assertions.assertEquals("ConcreteBuilder", builderDpSelectFields[1].getText());
		Assertions.assertEquals("Product", builderDpSelectFields[2].getText());

		builderDpSelectFields[2].setText(builderDpSelectFields[1].getText());
		Assertions.assertEquals("Please use unique names!",
				dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}

	public void testDuplicatesInSingleDpSelectField() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.retriveSelectDpComboBox().setSelectedItem("Bridge");
		JTextField[] bridgeDpSelectFields = dpGenerateForm.getParameters().values().toArray(new JTextField[0]);
		Assertions.assertEquals("ImplementorA,ImplementorB", bridgeDpSelectFields[3].getText());

		bridgeDpSelectFields[3].setText("ImplementorA,ImplementorA");
		Assertions.assertEquals("Please use unique names!",
				dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}

	public void testDuplicatesInProject() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.retriveSelectDpComboBox().setSelectedItem("Singleton");
		JTextField[] singletonDpSelectFields = dpGenerateForm.getParameters().values().toArray(new JTextField[0]);
		Assertions.assertEquals("Singleton", singletonDpSelectFields[0].getText());

		Assertions.assertEquals("File named Singleton.java already exists in selected package.",
				dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}

	public void testValidNameInDpSelectField() throws Exception {
		testInitalizeState();
		dpGenerateForm.retriveSelectDpRadioButton().doClick();
		Assertions.assertTrue(dpGenerateDialogWrapper.isOKActionEnabled());
		dpGenerateForm.retriveSelectDpComboBox().setSelectedItem("Singleton");
		JTextField[] singletonDpSelectFields = dpGenerateForm.getParameters().values().toArray(new JTextField[0]);
		Assertions.assertEquals("Singleton", singletonDpSelectFields[0].getText());

		singletonDpSelectFields[0].setText("Some.InVal@iDReferenceTyp3Name");
		Assertions.assertEquals("Please use valid identifiers!",
				dpGenerateDialogWrapper.doValidateAll().get(0).message);
	}
}