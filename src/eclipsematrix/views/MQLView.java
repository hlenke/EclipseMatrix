package eclipsematrix.views;

import java.util.LinkedList;
import java.util.List;

import matrix.db.Context;
import matrix.db.MQLCommand;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import eclipsematrix.EclipseMatrix;

/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 * 
 */

public class MQLView extends ViewPart {

	private Composite top = null;
	private Text txtOutput = null;
	private Label label = null;
	private Label lblInputBox = null;
	private Text txtInput = null;
	private Button cmdEnter = null;
	private int count = 0;
	private int current = 1;
	private List<String> history = new LinkedList<String>();
	private Button cmdClear = null;
	private Image imgClear;

	public MQLView() {
		// TODO Auto-generated constructor stub
	}

	public void createPartControl(Composite parent) {
		GridData gridData11 = new GridData();
		gridData11.horizontalAlignment = GridData.END;
		gridData11.verticalAlignment = GridData.CENTER;
		GridData gridData4 = new GridData();
		gridData4.horizontalAlignment = GridData.FILL;
		gridData4.verticalAlignment = GridData.CENTER;
		GridData gridData3 = new GridData();
		gridData3.horizontalSpan = 2;
		GridData gridData2 = new GridData();
		GridData gridData1 = new GridData();
		gridData1.horizontalAlignment = GridData.FILL;
		gridData1.grabExcessHorizontalSpace = true;
		gridData1.verticalAlignment = GridData.FILL;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		gridData.verticalAlignment = GridData.FILL;
		top = new Composite(parent, SWT.NONE);
		top.setLayout(gridLayout);
		label = new Label(top, SWT.NONE);
		label.setText("Output Pane");
		label.setLayoutData(gridData2);
		cmdClear = new Button(top, SWT.FLAT);
		// imgClear =
		// MxEclipsePlugin.getImageDescriptor("eraser.gif").createImage();
		cmdClear.setImage(imgClear);
		cmdClear.setToolTipText("Clear");
		cmdClear.setLayoutData(gridData11);
		cmdClear.setText("");
		cmdClear.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				txtOutput.setText("");
			}
		});
		txtOutput = new Text(top, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL
				| SWT.READ_ONLY);
		txtOutput.setEditable(true);
		txtOutput.setEnabled(true);
		txtOutput.setLayoutData(gridData);
		lblInputBox = new Label(top, SWT.NONE);
		lblInputBox.setText("Input MQL");
		lblInputBox.setLayoutData(gridData3);
		txtInput = new Text(top, SWT.BORDER);
		txtInput.setLayoutData(gridData1);

		txtInput.addKeyListener(new org.eclipse.swt.events.KeyAdapter() {
			public void keyReleased(final KeyEvent e) {
				if (e.keyCode == 13) {
					enterPressed();
				} else if (e.keyCode == 0x1000001) {
					// key up
					previousEntry();
				} else if (e.keyCode == 0x1000002) {
					// key down
					nextEntry();
				}
			}
		});
		cmdEnter = new Button(top, SWT.NONE);
		cmdEnter.setText("Enter");
		cmdEnter.setLayoutData(gridData4);
		cmdEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				enterPressed();
			}
		});

	}

	public void setFocus() {
		// TODO Auto-generated method stub

	}

	private void enterPressed() {
		count++;
		current = count;
		String inputText = txtInput.getText();
		txtOutput.append("MQL " + count + "> " + inputText + "\n");
		history.add(txtInput.getText());
		txtInput.setText("");
		System.out.println("test");

		Context context = EclipseMatrix.getDefault().getContext();
		System.out.println("context" + context.toString());
		if (context != null && context.isConnected()) {

			MQLCommand cmd = new MQLCommand();
			try {
				cmd.executeCommand(context, inputText);
				txtOutput.append(cmd.getError());
				txtOutput.append(cmd.getResult());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		} else {
			System.out.println("context null");
		}
	}

	private void previousEntry() {
		if (current > 0) {
			current--;
			txtInput.setText((String) history.get(current));
			txtInput.setSelection(((String) history.get(current)).length());
		}
	}

	private void nextEntry() {
		if (current < count - 1) {
			current++;
			txtInput.setText((String) history.get(current));
			txtInput.setSelection(((String) history.get(current)).length());
		}
	}
}
