package das.bbc;

import com.assafmanor.bbc.bbc.BBCMetaData;
import proto.types.meta.Meta;

public class MetaDataAdapter {
    static public BBCMetaData metaToBBCMeta(Meta toyMeta){
        return new BBCMetaData(toyMeta.getChannel(),toyMeta.getCid(),toyMeta.getCidSeries());
    }

}
