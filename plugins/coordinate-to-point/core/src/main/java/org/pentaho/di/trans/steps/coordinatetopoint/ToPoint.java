package org.pentaho.di.trans.steps.coordinatetopoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;

/**
 * @author shudongping
 * @date 2018/08/08
 */
public class ToPoint extends BaseStep implements StepInterface {

    private static Class<?> PKG = ToPoint.class;

    private ToPointMeta meta;

    private ToPointData data;

    /**
     *
     */
    public ToPoint(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        super(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }


    @Override
    public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
        meta = (ToPointMeta) smi;
        data = (ToPointData) sdi;

        Object[] r = getRow();

        if (r == null) {
            // no more input to be expected...
            setOutputDone();
            return false;
        }

        if (first) {
            first = false;

            data.rowMeta = getInputRowMeta().clone();

            meta.getFields(data.rowMeta, getStepname(), null, null, this, repository, metaStore);

            data.xFieldIndex = getInputRowMeta().indexOfValue(meta.getxField());
            data.yFieldIndex = getInputRowMeta().indexOfValue(meta.getyField());

        }
        Object[] outputRowData = r;
        if (r.length < getInputRowMeta().size() + 1) {
            outputRowData = RowDataUtil.resizeArray(r, getInputRowMeta().size() + 1);
        }


        Double xValue = getInputRowMeta().getNumber(r, data.xFieldIndex);

        Double yValue = getInputRowMeta().getNumber(r, data.yFieldIndex);

        //进行点空间转换
        Coordinate coordinate = new Coordinate(xValue, yValue);
        Geometry geometry = new GeometryFactory().createPoint(coordinate);
        outputRowData[getInputRowMeta().size()] = geometry;
        putRow(data.rowMeta, outputRowData);


        return true;


    }

    @Override
    public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
        boolean rCode = true;

        meta = (ToPointMeta) smi;
        data = (ToPointData) sdi;

        if (super.init(smi, sdi)) {

            return rCode;
        }
        return false;
    }

    @Override
    public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
        meta = (ToPointMeta) smi;
        data = (ToPointData) sdi;

        super.dispose(smi, sdi);
    }


}
