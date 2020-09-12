package das.bbc;

import com.assafmanor.bbc.bbc.BBCMetaData;
import proto.types.meta.Meta;

public class MetaDataAdapter {
    static public BBCMetaData metaToBBCMeta(Meta toyMeta,int height) {
        return new BBCMetaData(toyMeta.getChannel(), toyMeta.getCid(), toyMeta.getCidSeries(),height);
    }

    static public Meta bbcMetaToMeta(BBCMetaData bbcMetaData) {
        return Meta.newBuilder().setChannel(bbcMetaData.getChannel()).setCid(bbcMetaData.getCid()).setCidSeries(bbcMetaData.getCidSeries()).build();
    }

}
