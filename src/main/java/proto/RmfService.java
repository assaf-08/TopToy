// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: rmfService.proto

package proto;

public final class RmfService {
  private RmfService() {}
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
      "\n\020rmfService.proto\022\005proto\032\013types.proto2\213" +
      "\001\n\003Rmf\0221\n\022DisseminateMessage\022\013.proto.Dat" +
      "a\032\014.proto.Empty\"\000\022)\n\010FastVote\022\r.proto.Bb" +
      "cMsg\032\014.proto.Empty\"\000\022&\n\nreqMessage\022\n.pro" +
      "to.Req\032\n.proto.Res\"\000B\007\n\005protob\006proto3"
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
