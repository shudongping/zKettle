package org.pentaho.di.ui.trans.steps.coordinatetopoint;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.*;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.steps.coordinatetopoint.ToPointMeta;
import org.pentaho.di.ui.core.dialog.ErrorDialog;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

/**
 * @author shudongping
 * @date 2018/08/08
 */
public class ToPointDialog extends BaseStepDialog implements StepDialogInterface {


    private static Class<?> PKG = ToPointMeta.class;

    private boolean gotPreviousFields = false;
    private String[] fieldNames;

    private ToPointMeta input;


    private Label xField;
    private CCombo wxField;
    private FormData fdlxField, fdxField;

    private Label yField;
    private CCombo wyField;
    private FormData fdlyField, fdyField;


    private TextVar geomField;

    public ToPointDialog(Shell parent, Object in, TransMeta transMeta, String sname) {
        super(parent, (BaseStepMeta) in, transMeta, sname);
        input = (ToPointMeta) in;
    }


    @Override
    public String open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();

        shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);

        props.setLook(shell);
        setShellImage(shell, input);

        ModifyListener lsMod = new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                input.setChanged();
            }
        };
        changed = input.hasChanged();

        int middle = props.getMiddlePct();
        int margin = Const.MARGIN;

        FormLayout formLayout = new FormLayout();

        formLayout.marginWidth = Const.FORM_MARGIN;
        formLayout.marginHeight = Const.FORM_MARGIN;

        shell.setLayout(formLayout);
        shell.setText(BaseMessages.getString(PKG, "CoordinateToPoint.dialog"));
        getFields();

        wlStepname = new Label(shell, SWT.RIGHT);
        wlStepname.setText(BaseMessages.getString(PKG, "System.Label.StepName"));
        props.setLook(wlStepname);

        fdlStepname = new FormData();
        fdlStepname.left = new FormAttachment(0, 0);
        fdlStepname.right = new FormAttachment(middle, -margin);
        fdlStepname.top = new FormAttachment(0, margin);
        wlStepname.setLayoutData(fdlStepname);
        wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
        wStepname.setText(stepname);
        props.setLook(wStepname);
        wStepname.addModifyListener(lsMod);
        fdStepname = new FormData();
        fdStepname.left = new FormAttachment(middle, 0);
        fdStepname.top = new FormAttachment(0, margin);
        fdStepname.right = new FormAttachment(100, 0);
        wStepname.setLayoutData(fdStepname);

        //经度值选择
        xField = new Label(shell, SWT.RIGHT);
        xField.setText(BaseMessages.getString(PKG, "CoordinateToPointDialog.Xfield"));
        props.setLook(xField);
        fdlxField = new FormData();
        fdlxField.left = new FormAttachment(0, 0);
        fdlxField.top = new FormAttachment(wlStepname, 3 * margin);
        fdlxField.right = new FormAttachment(middle, -margin);
        xField.setLayoutData(fdlxField);
        wxField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wxField.setEditable(true);
        wxField.setItems(fieldNames);
        props.setLook(wxField);
        wxField.addModifyListener(lsMod);
        fdxField = new FormData();
        fdxField.left = new FormAttachment(middle, 0);
        fdxField.top = new FormAttachment(wStepname, margin);
        fdxField.right = new FormAttachment(100, 0);
        wxField.setLayoutData(fdxField);

        //纬度值选择
        yField = new Label(shell, SWT.RIGHT);
        yField.setText(BaseMessages.getString(PKG, "CoordinateToPointDialog.Yfield"));
        props.setLook(yField);
        fdlyField = new FormData();
        fdlyField.left = new FormAttachment(0, 0);
        fdlyField.top = new FormAttachment(xField, 3 * margin);
        fdlyField.right = new FormAttachment(middle, -margin);
        yField.setLayoutData(fdlyField);
        wyField = new CCombo(shell, SWT.BORDER | SWT.READ_ONLY);
        wyField.setEditable(true);
        wyField.setItems(fieldNames);
        props.setLook(wyField);
        wyField.addModifyListener(lsMod);
        fdyField = new FormData();
        fdyField.left = new FormAttachment(middle, 0);
        fdyField.top = new FormAttachment(wxField, margin);
        fdyField.right = new FormAttachment(100, 0);
        wyField.setLayoutData(fdyField);

        //空间字段输入框
        Label geomFieldname = new Label(shell, SWT.RIGHT);
        geomFieldname.setText(BaseMessages.getString(PKG,

                "CoordinateToPointDialog.Geomfield")); //$NON-NLS-1$

        //下面一行为控件设置用户定义的背景色和字体

        props.setLook(geomFieldname);

        FormData fdlFieldname = new FormData();

        fdlFieldname.left = new FormAttachment(0, 0);

        fdlFieldname.right = new FormAttachment(middle, -margin);

        fdlFieldname.top = new FormAttachment(yField, 5 * margin);

        geomFieldname.setLayoutData(fdlFieldname);
        geomField = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT
                | SWT.BORDER);

        props.setLook(geomField);

        geomField.addModifyListener(lsMod);

        FormData fdFieldname = new FormData();

        fdFieldname.left = new FormAttachment(middle, 0);

        fdFieldname.top = new FormAttachment(yField, 5 * margin);

        fdFieldname.right = new FormAttachment(100, 0);

        geomField.setLayoutData(fdFieldname);

        wOK = new Button(shell, SWT.PUSH);

        wOK.setText(BaseMessages.getString(PKG, "System.Button.OK")); //$NON-NLS-1$

        wCancel = new Button(shell, SWT.PUSH);

        wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel")); //$NON-NLS-1$

        setButtonPositions(new Button[]{wOK, wCancel}, margin, geomField);

        // Add listeners

        lsCancel = new Listener() {
            public void handleEvent(Event e) {
                cancel();
            }

        };

        lsOK = new Listener() {
            public void handleEvent(Event e) {
                ok();
            }

        };

        wCancel.addListener(SWT.Selection, lsCancel);

        wOK.addListener(SWT.Selection, lsOK);

        lsDef = new SelectionAdapter() {

            public void widgetDefaultSelected(SelectionEvent e) {

                ok();

            }

        };


        wStepname.addSelectionListener(lsDef);

        geomField.addSelectionListener(lsDef);


        // Detect X or ALT-F4 or something that kills this window...

        shell.addShellListener(new ShellAdapter() {//保证了窗口在非正常关闭时，取消用户的编辑

            public void shellClosed(ShellEvent e) {

                cancel();

            }

        });

        getData();

        setSize();

        input.setChanged(changed);

        shell.open();

        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }

        return stepname;

    }

    public void getData() {

        wStepname.selectAll();

        geomField.setText(Const.NVL(input.getGeomField(), ""));
        wxField.setText(input.getxField());
        wyField.setText(input.getyField());
    }


    private void getFields() {
        if (!gotPreviousFields) {
            try {
                RowMetaInterface r = transMeta.getPrevStepFields(stepname);
                if (r != null) {
                    fieldNames = r.getFieldNames();
                }
            } catch (KettleException ke) {
                new ErrorDialog(
                        shell, BaseMessages.getString(PKG, "CoordinateToPointDialog.FailedToGetFields.DialogTitle"),
                        BaseMessages.getString(PKG, "CoordinateToPointDialog.FailedToGetFields.DialogMessage"), ke);
            }
            gotPreviousFields = true;
        }
    }


    private void cancel() {

        stepname = null;

        input.setChanged(changed);

        dispose();

    }

    //单击OK把控件里用户输入的数据都写入到步骤的元数据对象中。

    private void ok() {

        if (Const.isEmpty(wStepname.getText()))
            return;

        stepname = wStepname.getText(); // return value

        input.setxField(wxField.getText());
        input.setyField(wyField.getText());
        input.setGeomField(geomField.getText());

        dispose();

    }


}
