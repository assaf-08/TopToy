// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: obbc.proto

package proto;

public final class ObbcOuterClass {
  private ObbcOuterClass() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\nobbc.proto\022\005proto\032\013types.proto2q\n\004Obbc" +
      "\022)\n\010FastVote\022\r.proto.BbcMsg\032\014.proto.Empt" +
      "y\"\000\022>\n\022EvidenceReqMessage\022\022.proto.Eviden" +
      "ceReq\032\022.proto.EvidenceRes\"\000B\007\n\005protob\006pr" +
      "oto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          proto.Types.getDescriptor(),
        }, assigner);
    proto.Types.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
