// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: syncService.proto

package proto;

public interface subChainVersionOrBuilder extends
    // @@protoc_insertion_point(interface_extends:proto.subChainVersion)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>int32 forkPoint = 1;</code>
   */
  int getForkPoint();

  /**
   * <code>int32 suggested = 2;</code>
   */
  int getSuggested();

  /**
   * <pre>
   *    repeated frontSupport fp = 3;
   * </pre>
   *
   * <code>repeated .proto.proofedBlock v = 3;</code>
   */
  java.util.List<proto.proofedBlock> 
      getVList();
  /**
   * <pre>
   *    repeated frontSupport fp = 3;
   * </pre>
   *
   * <code>repeated .proto.proofedBlock v = 3;</code>
   */
  proto.proofedBlock getV(int index);
  /**
   * <pre>
   *    repeated frontSupport fp = 3;
   * </pre>
   *
   * <code>repeated .proto.proofedBlock v = 3;</code>
   */
  int getVCount();
  /**
   * <pre>
   *    repeated frontSupport fp = 3;
   * </pre>
   *
   * <code>repeated .proto.proofedBlock v = 3;</code>
   */
  java.util.List<? extends proto.proofedBlockOrBuilder> 
      getVOrBuilderList();
  /**
   * <pre>
   *    repeated frontSupport fp = 3;
   * </pre>
   *
   * <code>repeated .proto.proofedBlock v = 3;</code>
   */
  proto.proofedBlockOrBuilder getVOrBuilder(
      int index);

  /**
   * <code>int32 sender = 4;</code>
   */
  int getSender();
}