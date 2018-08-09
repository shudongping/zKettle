package org.pentaho.di.trans.steps.coordinatetopoint;

import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.geospatial.SRS;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.row.ValueMetaInterface;
import org.pentaho.di.core.row.value.ValueMetaGeometry;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.*;
import org.pentaho.di.ui.trans.steps.coordinatetopoint.ToPointDialog;
import org.pentaho.metastore.api.IMetaStore;
import org.w3c.dom.Node;

import java.util.List;

/**
 * @author shudongping
 * @date 2018/08/08
 */

@Step(id = "CoordinateToPoint", image = "JSI.svg", i18nPackageName = "org.pentaho.di.trans.steps.coordinatetopoint",
        documentationUrl = "http://wiki.pentaho.com/display/EAI/JSON+Input", name = "CoordinateToPoint.name",
        description = "CoordinateToPoint.description", categoryDescription = "i18n:org.pentaho.di.trans.step:BaseStep.Category.Transform")
public class ToPointMeta extends BaseStepMeta implements StepMetaInterface {


    private static Class<?> PKG = ToPointMeta.class;

    /**
     * 点经度字段
     */
    private String xField;

    /**
     * 点纬度字段
     */
    private String yField;

    /**
     * 空间字段
     */
    private String geomField;

    public String getGeomField() {
        return geomField;
    }

    public void setGeomField(String geomField) {
        this.geomField = geomField;
    }

    public String getxField() {
        return xField;
    }

    public void setxField(String xField) {
        this.xField = xField;
    }

    public String getyField() {
        return yField;
    }

    public void setyField(String yField) {
        this.yField = yField;
    }


    @Override
    public void loadXML(Node stepnode, List<DatabaseMeta> databases, IMetaStore metaStore) throws KettleXMLException {
        readData(stepnode);
    }


    @Override
    public String getXML() {
        StringBuilder retval = new StringBuilder();

        retval.append("    " + XMLHandler.addTagValue("x_field", xField));
        retval.append("    " + XMLHandler.addTagValue("y_field", yField));
        retval.append("    " + XMLHandler.addTagValue("geom_field", geomField));

        return retval.toString();
    }

    private void readData(Node stepnode) throws KettleXMLException {

        try {
            xField = XMLHandler.getTagValue(stepnode, "x_field");
            yField = XMLHandler.getTagValue(stepnode, "y_field");
            geomField = XMLHandler.getTagValue(stepnode, "geom_field");
        } catch (Exception e) {
            throw new KettleXMLException("Unable to load step info from XML", e);
        }

    }


    @Override
    public void readRep(Repository rep, IMetaStore metaStore, ObjectId id_step, List<DatabaseMeta> databases) throws KettleException {

        try {
            xField = rep.getStepAttributeString(id_step, "x_field");
            yField = rep.getStepAttributeString(id_step, "y_field");
            geomField = rep.getStepAttributeString(id_step, "geom_field");
        } catch (Exception e) {
            throw new KettleException("读取错误", e);
        }

    }

    @Override
    public void saveRep(Repository rep, IMetaStore metaStore, ObjectId id_transformation, ObjectId id_step) throws KettleException {
        try {
            rep.saveStepAttribute(id_transformation, id_step, "x_field", xField);
            rep.saveStepAttribute(id_transformation, id_step, "y_field", yField);
            rep.saveStepAttribute(id_transformation, id_step, "geom_field", geomField);
        } catch (Exception e) {
            throw new KettleException("Unable to save step information to the repository for id_step=" + id_step, e);
        }
    }

    /**
     * 添加转换之后的空间字段  暂时定为the_geom
     */
    @Override
    public void getFields(RowMetaInterface inputRowMeta, String name, RowMetaInterface[] info, StepMeta nextStep,
                          VariableSpace space, Repository repository, IMetaStore metaStore) throws KettleStepException {

        ValueMetaInterface v = new ValueMetaGeometry();
        v.setName(geomField);
        v.setOrigin(geomField);
//        v.setGeometrySRS(new SRS(DefaultGeographicCRS.WGS84));
        inputRowMeta.addValueMeta(v);
    }

    @Override
    public void setDefault() {
        xField = null;
        yField = null;
        geomField = "the_geom";
    }

    @Override
    public StepInterface getStep(StepMeta stepMeta, StepDataInterface stepDataInterface, int copyNr, TransMeta transMeta, Trans trans) {
        return new ToPoint(stepMeta, stepDataInterface, copyNr, transMeta, trans);
    }

    @Override
    public StepDataInterface getStepData() {
        return new ToPointData();
    }

    public String getDialogClassName(){
        return ToPointDialog.class.getName();
    }

}
