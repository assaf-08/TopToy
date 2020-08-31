package das.bbc;

import com.assafmanor.bbc.bbc.BBCMetaData;
import proto.types.meta.Meta;

public class MetaDataAdapter {
    static public BBCMetaData metaToBBCMeta(Meta toyMeta) {
        return new BBCMetaData(toyMeta.getChannel(), toyMeta.getCid(), toyMeta.getCidSeries());
    }

    static public Meta bbcMetaToMeta(BBCMetaData bbcMetaData) {
        return Meta.newBuilder().setChannel(bbcMetaData.getChannel()).setCid(bbcMetaData.getCid()).setCidSeries(bbcMetaData.getCidSeries()).build();
    }

}
