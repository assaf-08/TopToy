// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: rmfService.proto

package proto;

/**
 * Protobuf type {@code proto.FastBbcVote}
 */
public  final class FastBbcVote extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:proto.FastBbcVote)
    FastBbcVoteOrBuilder {
private static final long serialVersionUID = 0L;
  // Use FastBbcVote.newBuilder() to construct.
  private FastBbcVote(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
    super(builder);
  }
  private FastBbcVote() {
    vote_ = 0;
  }

  @java.lang.Override
  public final com.google.protobuf.UnknownFieldSet
  getUnknownFields() {
    return this.unknownFields;
  }
  private FastBbcVote(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    this();
    if (extensionRegistry == null) {
      throw new java.lang.NullPointerException();
    }
    int mutable_bitField0_ = 0;
    com.google.protobuf.UnknownFieldSet.Builder unknownFields =
        com.google.protobuf.UnknownFieldSet.newBuilder();
    try {
      boolean done = false;
      while (!done) {
        int tag = input.readTag();
        switch (tag) {
          case 0:
            done = true;
            break;
          default: {
            if (!parseUnknownFieldProto3(
                input, unknownFields, extensionRegistry, tag)) {
              done = true;
            }
            break;
          }
          case 10: {
            proto.Meta.Builder subBuilder = null;
            if (Meta_ != null) {
              subBuilder = Meta_.toBuilder();
            }
            Meta_ = input.readMessage(proto.Meta.parser(), extensionRegistry);
            if (subBuilder != null) {
              subBuilder.mergeFrom(Meta_);
              Meta_ = subBuilder.buildPartial();
            }

            break;
          }
          case 16: {

            vote_ = input.readInt32();
            break;
          }
        }
      }
    } catch (com.google.protobuf.InvalidProtocolBufferException e) {
      throw e.setUnfinishedMessage(this);
    } catch (java.io.IOException e) {
      throw new com.google.protobuf.InvalidProtocolBufferException(
          e).setUnfinishedMessage(this);
    } finally {
      this.unknownFields = unknownFields.build();
      makeExtensionsImmutable();
    }
  }
  public static final com.google.protobuf.Descriptors.Descriptor
      getDescriptor() {
    return proto.RmfService.internal_static_proto_FastBbcVote_descriptor;
  }

  protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internalGetFieldAccessorTable() {
    return proto.RmfService.internal_static_proto_FastBbcVote_fieldAccessorTable
        .ensureFieldAccessorsInitialized(
            proto.FastBbcVote.class, proto.FastBbcVote.Builder.class);
  }

  public static final int _META_FIELD_NUMBER = 1;
  private proto.Meta Meta_;
  /**
   * <code>.proto.Meta _meta = 1;</code>
   */
  public boolean hasMeta() {
    return Meta_ != null;
  }
  /**
   * <code>.proto.Meta _meta = 1;</code>
   */
  public proto.Meta getMeta() {
    return Meta_ == null ? proto.Meta.getDefaultInstance() : Meta_;
  }
  /**
   * <code>.proto.Meta _meta = 1;</code>
   */
  public proto.MetaOrBuilder getMetaOrBuilder() {
    return getMeta();
  }

  public static final int VOTE_FIELD_NUMBER = 2;
  private int vote_;
  /**
   * <code>int32 vote = 2;</code>
   */
  public int getVote() {
    return vote_;
  }

  private byte memoizedIsInitialized = -1;
  public final boolean isInitialized() {
    byte isInitialized = memoizedIsInitialized;
    if (isInitialized == 1) return true;
    if (isInitialized == 0) return false;

    memoizedIsInitialized = 1;
    return true;
  }

  public void writeTo(com.google.protobuf.CodedOutputStream output)
                      throws java.io.IOException {
    if (Meta_ != null) {
      output.writeMessage(1, getMeta());
    }
    if (vote_ != 0) {
      output.writeInt32(2, vote_);
    }
    unknownFields.writeTo(output);
  }

  public int getSerializedSize() {
    int size = memoizedSize;
    if (size != -1) return size;

    size = 0;
    if (Meta_ != null) {
      size += com.google.protobuf.CodedOutputStream
        .computeMessageSize(1, getMeta());
    }
    if (vote_ != 0) {
      size += com.google.protobuf.CodedOutputStream
        .computeInt32Size(2, vote_);
    }
    size += unknownFields.getSerializedSize();
    memoizedSize = size;
    return size;
  }

  @java.lang.Override
  public boolean equals(final java.lang.Object obj) {
    if (obj == this) {
     return true;
    }
    if (!(obj instanceof proto.FastBbcVote)) {
      return super.equals(obj);
    }
    proto.FastBbcVote other = (proto.FastBbcVote) obj;

    boolean result = true;
    result = result && (hasMeta() == other.hasMeta());
    if (hasMeta()) {
      result = result && getMeta()
          .equals(other.getMeta());
    }
    result = result && (getVote()
        == other.getVote());
    result = result && unknownFields.equals(other.unknownFields);
    return result;
  }

  @java.lang.Override
  public int hashCode() {
    if (memoizedHashCode != 0) {
      return memoizedHashCode;
    }
    int hash = 41;
    hash = (19 * hash) + getDescriptor().hashCode();
    if (hasMeta()) {
      hash = (37 * hash) + _META_FIELD_NUMBER;
      hash = (53 * hash) + getMeta().hashCode();
    }
    hash = (37 * hash) + VOTE_FIELD_NUMBER;
    hash = (53 * hash) + getVote();
    hash = (29 * hash) + unknownFields.hashCode();
    memoizedHashCode = hash;
    return hash;
  }

  public static proto.FastBbcVote parseFrom(
      java.nio.ByteBuffer data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.FastBbcVote parseFrom(
      java.nio.ByteBuffer data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.FastBbcVote parseFrom(
      com.google.protobuf.ByteString data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.FastBbcVote parseFrom(
      com.google.protobuf.ByteString data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.FastBbcVote parseFrom(byte[] data)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data);
  }
  public static proto.FastBbcVote parseFrom(
      byte[] data,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws com.google.protobuf.InvalidProtocolBufferException {
    return PARSER.parseFrom(data, extensionRegistry);
  }
  public static proto.FastBbcVote parseFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.FastBbcVote parseFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.FastBbcVote parseDelimitedFrom(java.io.InputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input);
  }
  public static proto.FastBbcVote parseDelimitedFrom(
      java.io.InputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
  }
  public static proto.FastBbcVote parseFrom(
      com.google.protobuf.CodedInputStream input)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input);
  }
  public static proto.FastBbcVote parseFrom(
      com.google.protobuf.CodedInputStream input,
      com.google.protobuf.ExtensionRegistryLite extensionRegistry)
      throws java.io.IOException {
    return com.google.protobuf.GeneratedMessageV3
        .parseWithIOException(PARSER, input, extensionRegistry);
  }

  public Builder newBuilderForType() { return newBuilder(); }
  public static Builder newBuilder() {
    return DEFAULT_INSTANCE.toBuilder();
  }
  public static Builder newBuilder(proto.FastBbcVote prototype) {
    return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
  }
  public Builder toBuilder() {
    return this == DEFAULT_INSTANCE
        ? new Builder() : new Builder().mergeFrom(this);
  }

  @java.lang.Override
  protected Builder newBuilderForType(
      com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
    Builder builder = new Builder(parent);
    return builder;
  }
  /**
   * Protobuf type {@code proto.FastBbcVote}
   */
  public static final class Builder extends
      com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
      // @@protoc_insertion_point(builder_implements:proto.FastBbcVote)
      proto.FastBbcVoteOrBuilder {
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return proto.RmfService.internal_static_proto_FastBbcVote_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.RmfService.internal_static_proto_FastBbcVote_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.FastBbcVote.class, proto.FastBbcVote.Builder.class);
    }

    // Construct using proto.FastBbcVote.newBuilder()
    private Builder() {
      maybeForceBuilderInitialization();
    }

    private Builder(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      super(parent);
      maybeForceBuilderInitialization();
    }
    private void maybeForceBuilderInitialization() {
      if (com.google.protobuf.GeneratedMessageV3
              .alwaysUseFieldBuilders) {
      }
    }
    public Builder clear() {
      super.clear();
      if (MetaBuilder_ == null) {
        Meta_ = null;
      } else {
        Meta_ = null;
        MetaBuilder_ = null;
      }
      vote_ = 0;

      return this;
    }

    public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
      return proto.RmfService.internal_static_proto_FastBbcVote_descriptor;
    }

    public proto.FastBbcVote getDefaultInstanceForType() {
      return proto.FastBbcVote.getDefaultInstance();
    }

    public proto.FastBbcVote build() {
      proto.FastBbcVote result = buildPartial();
      if (!result.isInitialized()) {
        throw newUninitializedMessageException(result);
      }
      return result;
    }

    public proto.FastBbcVote buildPartial() {
      proto.FastBbcVote result = new proto.FastBbcVote(this);
      if (MetaBuilder_ == null) {
        result.Meta_ = Meta_;
      } else {
        result.Meta_ = MetaBuilder_.build();
      }
      result.vote_ = vote_;
      onBuilt();
      return result;
    }

    public Builder clone() {
      return (Builder) super.clone();
    }
    public Builder setField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.setField(field, value);
    }
    public Builder clearField(
        com.google.protobuf.Descriptors.FieldDescriptor field) {
      return (Builder) super.clearField(field);
    }
    public Builder clearOneof(
        com.google.protobuf.Descriptors.OneofDescriptor oneof) {
      return (Builder) super.clearOneof(oneof);
    }
    public Builder setRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        int index, java.lang.Object value) {
      return (Builder) super.setRepeatedField(field, index, value);
    }
    public Builder addRepeatedField(
        com.google.protobuf.Descriptors.FieldDescriptor field,
        java.lang.Object value) {
      return (Builder) super.addRepeatedField(field, value);
    }
    public Builder mergeFrom(com.google.protobuf.Message other) {
      if (other instanceof proto.FastBbcVote) {
        return mergeFrom((proto.FastBbcVote)other);
      } else {
        super.mergeFrom(other);
        return this;
      }
    }

    public Builder mergeFrom(proto.FastBbcVote other) {
      if (other == proto.FastBbcVote.getDefaultInstance()) return this;
      if (other.hasMeta()) {
        mergeMeta(other.getMeta());
      }
      if (other.getVote() != 0) {
        setVote(other.getVote());
      }
      this.mergeUnknownFields(other.unknownFields);
      onChanged();
      return this;
    }

    public final boolean isInitialized() {
      return true;
    }

    public Builder mergeFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      proto.FastBbcVote parsedMessage = null;
      try {
        parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        parsedMessage = (proto.FastBbcVote) e.getUnfinishedMessage();
        throw e.unwrapIOException();
      } finally {
        if (parsedMessage != null) {
          mergeFrom(parsedMessage);
        }
      }
      return this;
    }

    private proto.Meta Meta_ = null;
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.Meta, proto.Meta.Builder, proto.MetaOrBuilder> MetaBuilder_;
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public boolean hasMeta() {
      return MetaBuilder_ != null || Meta_ != null;
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public proto.Meta getMeta() {
      if (MetaBuilder_ == null) {
        return Meta_ == null ? proto.Meta.getDefaultInstance() : Meta_;
      } else {
        return MetaBuilder_.getMessage();
      }
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public Builder setMeta(proto.Meta value) {
      if (MetaBuilder_ == null) {
        if (value == null) {
          throw new NullPointerException();
        }
        Meta_ = value;
        onChanged();
      } else {
        MetaBuilder_.setMessage(value);
      }

      return this;
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public Builder setMeta(
        proto.Meta.Builder builderForValue) {
      if (MetaBuilder_ == null) {
        Meta_ = builderForValue.build();
        onChanged();
      } else {
        MetaBuilder_.setMessage(builderForValue.build());
      }

      return this;
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public Builder mergeMeta(proto.Meta value) {
      if (MetaBuilder_ == null) {
        if (Meta_ != null) {
          Meta_ =
            proto.Meta.newBuilder(Meta_).mergeFrom(value).buildPartial();
        } else {
          Meta_ = value;
        }
        onChanged();
      } else {
        MetaBuilder_.mergeFrom(value);
      }

      return this;
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public Builder clearMeta() {
      if (MetaBuilder_ == null) {
        Meta_ = null;
        onChanged();
      } else {
        Meta_ = null;
        MetaBuilder_ = null;
      }

      return this;
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public proto.Meta.Builder getMetaBuilder() {
      
      onChanged();
      return getMetaFieldBuilder().getBuilder();
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    public proto.MetaOrBuilder getMetaOrBuilder() {
      if (MetaBuilder_ != null) {
        return MetaBuilder_.getMessageOrBuilder();
      } else {
        return Meta_ == null ?
            proto.Meta.getDefaultInstance() : Meta_;
      }
    }
    /**
     * <code>.proto.Meta _meta = 1;</code>
     */
    private com.google.protobuf.SingleFieldBuilderV3<
        proto.Meta, proto.Meta.Builder, proto.MetaOrBuilder> 
        getMetaFieldBuilder() {
      if (MetaBuilder_ == null) {
        MetaBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
            proto.Meta, proto.Meta.Builder, proto.MetaOrBuilder>(
                getMeta(),
                getParentForChildren(),
                isClean());
        Meta_ = null;
      }
      return MetaBuilder_;
    }

    private int vote_ ;
    /**
     * <code>int32 vote = 2;</code>
     */
    public int getVote() {
      return vote_;
    }
    /**
     * <code>int32 vote = 2;</code>
     */
    public Builder setVote(int value) {
      
      vote_ = value;
      onChanged();
      return this;
    }
    /**
     * <code>int32 vote = 2;</code>
     */
    public Builder clearVote() {
      
      vote_ = 0;
      onChanged();
      return this;
    }
    public final Builder setUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.setUnknownFieldsProto3(unknownFields);
    }

    public final Builder mergeUnknownFields(
        final com.google.protobuf.UnknownFieldSet unknownFields) {
      return super.mergeUnknownFields(unknownFields);
    }


    // @@protoc_insertion_point(builder_scope:proto.FastBbcVote)
  }

  // @@protoc_insertion_point(class_scope:proto.FastBbcVote)
  private static final proto.FastBbcVote DEFAULT_INSTANCE;
  static {
    DEFAULT_INSTANCE = new proto.FastBbcVote();
  }

  public static proto.FastBbcVote getDefaultInstance() {
    return DEFAULT_INSTANCE;
  }

  private static final com.google.protobuf.Parser<FastBbcVote>
      PARSER = new com.google.protobuf.AbstractParser<FastBbcVote>() {
    public FastBbcVote parsePartialFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return new FastBbcVote(input, extensionRegistry);
    }
  };

  public static com.google.protobuf.Parser<FastBbcVote> parser() {
    return PARSER;
  }

  @java.lang.Override
  public com.google.protobuf.Parser<FastBbcVote> getParserForType() {
    return PARSER;
  }

  public proto.FastBbcVote getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

}

