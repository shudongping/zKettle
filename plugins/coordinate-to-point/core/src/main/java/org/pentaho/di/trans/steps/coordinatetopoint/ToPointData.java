package org.pentaho.di.trans.steps.coordinatetopoint;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

/**
 * @author shudongping
 * @date 2018/08/08
 */
public class ToPointData extends BaseStepData implements StepDataInterface {


    public RowMetaInterface rowMeta;

    public int xFieldIndex;

    public int yFieldIndex;


    public ToPointData(){
        super();
    }


}
