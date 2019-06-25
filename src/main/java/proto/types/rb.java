// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: types/rb.proto

package proto.types;

public final class rb {
  private rb() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface RBMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:proto.types.RBMsg)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    boolean hasM();
    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    proto.types.meta.Meta getM();
    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    proto.types.meta.MetaOrBuilder getMOrBuilder();

    /**
     * <code>int32 type = 2;</code>
     */
    int getType();

    /**
     * <code>bytes data = 3;</code>
     */
    com.google.protobuf.ByteString getData();

    /**
     * <code>int32 sender = 4;</code>
     */
    int getSender();
  }
  /**
   * Protobuf type {@code proto.types.RBMsg}
   */
  public  static final class RBMsg extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:proto.types.RBMsg)
      RBMsgOrBuilder {
  private static final long serialVersionUID = 0L;
    // Use RBMsg.newBuilder() to construct.
    private RBMsg(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private RBMsg() {
      data_ = com.google.protobuf.ByteString.EMPTY;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return this.unknownFields;
    }
    private RBMsg(
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
            case 10: {
              proto.types.meta.Meta.Builder subBuilder = null;
              if (m_ != null) {
                subBuilder = m_.toBuilder();
              }
              m_ = input.readMessage(proto.types.meta.Meta.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(m_);
                m_ = subBuilder.buildPartial();
              }

              break;
            }
            case 16: {

              type_ = input.readInt32();
              break;
            }
            case 26: {

              data_ = input.readBytes();
              break;
            }
            case 32: {

              sender_ = input.readInt32();
              break;
            }
            default: {
              if (!parseUnknownField(
                  input, unknownFields, extensionRegistry, tag)) {
                done = true;
              }
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
      return proto.types.rb.internal_static_proto_types_RBMsg_descriptor;
    }

    @java.lang.Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return proto.types.rb.internal_static_proto_types_RBMsg_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              proto.types.rb.RBMsg.class, proto.types.rb.RBMsg.Builder.class);
    }

    public static final int M_FIELD_NUMBER = 1;
    private proto.types.meta.Meta m_;
    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    public boolean hasM() {
      return m_ != null;
    }
    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    public proto.types.meta.Meta getM() {
      return m_ == null ? proto.types.meta.Meta.getDefaultInstance() : m_;
    }
    /**
     * <code>.proto.types.Meta m = 1;</code>
     */
    public proto.types.meta.MetaOrBuilder getMOrBuilder() {
      return getM();
    }

    public static final int TYPE_FIELD_NUMBER = 2;
    private int type_;
    /**
     * <code>int32 type = 2;</code>
     */
    public int getType() {
      return type_;
    }

    public static final int DATA_FIELD_NUMBER = 3;
    private com.google.protobuf.ByteString data_;
    /**
     * <code>bytes data = 3;</code>
     */
    public com.google.protobuf.ByteString getData() {
      return data_;
    }

    public static final int SENDER_FIELD_NUMBER = 4;
    private int sender_;
    /**
     * <code>int32 sender = 4;</code>
     */
    public int getSender() {
      return sender_;
    }

    private byte memoizedIsInitialized = -1;
    @java.lang.Override
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    @java.lang.Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (m_ != null) {
        output.writeMessage(1, getM());
      }
      if (type_ != 0) {
        output.writeInt32(2, type_);
      }
      if (!data_.isEmpty()) {
        output.writeBytes(3, data_);
      }
      if (sender_ != 0) {
        output.writeInt32(4, sender_);
      }
      unknownFields.writeTo(output);
    }

    @java.lang.Override
    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (m_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getM());
      }
      if (type_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, type_);
      }
      if (!data_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, data_);
      }
      if (sender_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(4, sender_);
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
      if (!(obj instanceof proto.types.rb.RBMsg)) {
        return super.equals(obj);
      }
      proto.types.rb.RBMsg other = (proto.types.rb.RBMsg) obj;

      if (hasM() != other.hasM()) return false;
      if (hasM()) {
        if (!getM()
            .equals(other.getM())) return false;
      }
      if (getType()
          != other.getType()) return false;
      if (!getData()
          .equals(other.getData())) return false;
      if (getSender()
          != other.getSender()) return false;
      if (!unknownFields.equals(other.unknownFields)) return false;
      return true;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptor().hashCode();
      if (hasM()) {
        hash = (37 * hash) + M_FIELD_NUMBER;
        hash = (53 * hash) + getM().hashCode();
      }
      hash = (37 * hash) + TYPE_FIELD_NUMBER;
      hash = (53 * hash) + getType();
      hash = (37 * hash) + DATA_FIELD_NUMBER;
      hash = (53 * hash) + getData().hashCode();
      hash = (37 * hash) + SENDER_FIELD_NUMBER;
      hash = (53 * hash) + getSender();
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static proto.types.rb.RBMsg parseFrom(
        java.nio.ByteBuffer data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.types.rb.RBMsg parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.types.rb.RBMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.types.rb.RBMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.types.rb.RBMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static proto.types.rb.RBMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static proto.types.rb.RBMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static proto.types.rb.RBMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static proto.types.rb.RBMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static proto.types.rb.RBMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static proto.types.rb.RBMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static proto.types.rb.RBMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    @java.lang.Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(proto.types.rb.RBMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @java.lang.Override
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
     * Protobuf type {@code proto.types.RBMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:proto.types.RBMsg)
        proto.types.rb.RBMsgOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return proto.types.rb.internal_static_proto_types_RBMsg_descriptor;
      }

      @java.lang.Override
      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return proto.types.rb.internal_static_proto_types_RBMsg_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                proto.types.rb.RBMsg.class, proto.types.rb.RBMsg.Builder.class);
      }

      // Construct using proto.types.rb.RBMsg.newBuilder()
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
      @java.lang.Override
      public Builder clear() {
        super.clear();
        if (mBuilder_ == null) {
          m_ = null;
        } else {
          m_ = null;
          mBuilder_ = null;
        }
        type_ = 0;

        data_ = com.google.protobuf.ByteString.EMPTY;

        sender_ = 0;

        return this;
      }

      @java.lang.Override
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return proto.types.rb.internal_static_proto_types_RBMsg_descriptor;
      }

      @java.lang.Override
      public proto.types.rb.RBMsg getDefaultInstanceForType() {
        return proto.types.rb.RBMsg.getDefaultInstance();
      }

      @java.lang.Override
      public proto.types.rb.RBMsg build() {
        proto.types.rb.RBMsg result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      @java.lang.Override
      public proto.types.rb.RBMsg buildPartial() {
        proto.types.rb.RBMsg result = new proto.types.rb.RBMsg(this);
        if (mBuilder_ == null) {
          result.m_ = m_;
        } else {
          result.m_ = mBuilder_.build();
        }
        result.type_ = type_;
        result.data_ = data_;
        result.sender_ = sender_;
        onBuilt();
        return result;
      }

      @java.lang.Override
      public Builder clone() {
        return super.clone();
      }
      @java.lang.Override
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.setField(field, value);
      }
      @java.lang.Override
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return super.clearField(field);
      }
      @java.lang.Override
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return super.clearOneof(oneof);
      }
      @java.lang.Override
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, java.lang.Object value) {
        return super.setRepeatedField(field, index, value);
      }
      @java.lang.Override
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          java.lang.Object value) {
        return super.addRepeatedField(field, value);
      }
      @java.lang.Override
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof proto.types.rb.RBMsg) {
          return mergeFrom((proto.types.rb.RBMsg)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(proto.types.rb.RBMsg other) {
        if (other == proto.types.rb.RBMsg.getDefaultInstance()) return this;
        if (other.hasM()) {
          mergeM(other.getM());
        }
        if (other.getType() != 0) {
          setType(other.getType());
        }
        if (other.getData() != com.google.protobuf.ByteString.EMPTY) {
          setData(other.getData());
        }
        if (other.getSender() != 0) {
          setSender(other.getSender());
        }
        this.mergeUnknownFields(other.unknownFields);
        onChanged();
        return this;
      }

      @java.lang.Override
      public final boolean isInitialized() {
        return true;
      }

      @java.lang.Override
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        proto.types.rb.RBMsg parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (proto.types.rb.RBMsg) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private proto.types.meta.Meta m_;
      private com.google.protobuf.SingleFieldBuilderV3<
          proto.types.meta.Meta, proto.types.meta.Meta.Builder, proto.types.meta.MetaOrBuilder> mBuilder_;
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public boolean hasM() {
        return mBuilder_ != null || m_ != null;
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public proto.types.meta.Meta getM() {
        if (mBuilder_ == null) {
          return m_ == null ? proto.types.meta.Meta.getDefaultInstance() : m_;
        } else {
          return mBuilder_.getMessage();
        }
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public Builder setM(proto.types.meta.Meta value) {
        if (mBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          m_ = value;
          onChanged();
        } else {
          mBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public Builder setM(
          proto.types.meta.Meta.Builder builderForValue) {
        if (mBuilder_ == null) {
          m_ = builderForValue.build();
          onChanged();
        } else {
          mBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public Builder mergeM(proto.types.meta.Meta value) {
        if (mBuilder_ == null) {
          if (m_ != null) {
            m_ =
              proto.types.meta.Meta.newBuilder(m_).mergeFrom(value).buildPartial();
          } else {
            m_ = value;
          }
          onChanged();
        } else {
          mBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public Builder clearM() {
        if (mBuilder_ == null) {
          m_ = null;
          onChanged();
        } else {
          m_ = null;
          mBuilder_ = null;
        }

        return this;
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public proto.types.meta.Meta.Builder getMBuilder() {
        
        onChanged();
        return getMFieldBuilder().getBuilder();
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      public proto.types.meta.MetaOrBuilder getMOrBuilder() {
        if (mBuilder_ != null) {
          return mBuilder_.getMessageOrBuilder();
        } else {
          return m_ == null ?
              proto.types.meta.Meta.getDefaultInstance() : m_;
        }
      }
      /**
       * <code>.proto.types.Meta m = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          proto.types.meta.Meta, proto.types.meta.Meta.Builder, proto.types.meta.MetaOrBuilder> 
          getMFieldBuilder() {
        if (mBuilder_ == null) {
          mBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              proto.types.meta.Meta, proto.types.meta.Meta.Builder, proto.types.meta.MetaOrBuilder>(
                  getM(),
                  getParentForChildren(),
                  isClean());
          m_ = null;
        }
        return mBuilder_;
      }

      private int type_ ;
      /**
       * <code>int32 type = 2;</code>
       */
      public int getType() {
        return type_;
      }
      /**
       * <code>int32 type = 2;</code>
       */
      public Builder setType(int value) {
        
        type_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 type = 2;</code>
       */
      public Builder clearType() {
        
        type_ = 0;
        onChanged();
        return this;
      }

      private com.google.protobuf.ByteString data_ = com.google.protobuf.ByteString.EMPTY;
      /**
       * <code>bytes data = 3;</code>
       */
      public com.google.protobuf.ByteString getData() {
        return data_;
      }
      /**
       * <code>bytes data = 3;</code>
       */
      public Builder setData(com.google.protobuf.ByteString value) {
        if (value == null) {
    throw new NullPointerException();
  }
  
        data_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>bytes data = 3;</code>
       */
      public Builder clearData() {
        
        data_ = getDefaultInstance().getData();
        onChanged();
        return this;
      }

      private int sender_ ;
      /**
       * <code>int32 sender = 4;</code>
       */
      public int getSender() {
        return sender_;
      }
      /**
       * <code>int32 sender = 4;</code>
       */
      public Builder setSender(int value) {
        
        sender_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>int32 sender = 4;</code>
       */
      public Builder clearSender() {
        
        sender_ = 0;
        onChanged();
        return this;
      }
      @java.lang.Override
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.setUnknownFields(unknownFields);
      }

      @java.lang.Override
      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return super.mergeUnknownFields(unknownFields);
      }


      // @@protoc_insertion_point(builder_scope:proto.types.RBMsg)
    }

    // @@protoc_insertion_point(class_scope:proto.types.RBMsg)
    private static final proto.types.rb.RBMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new proto.types.rb.RBMsg();
    }

    public static proto.types.rb.RBMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<RBMsg>
        PARSER = new com.google.protobuf.AbstractParser<RBMsg>() {
      @java.lang.Override
      public RBMsg parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
        return new RBMsg(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<RBMsg> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<RBMsg> getParserForType() {
      return PARSER;
    }

    @java.lang.Override
    public proto.types.rb.RBMsg getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_types_RBMsg_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_types_RBMsg_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\016types/rb.proto\022\013proto.types\032\020types/met" +
      "a.proto\"Q\n\005RBMsg\022\034\n\001m\030\001 \001(\0132\021.proto.type" +
      "s.Meta\022\014\n\004type\030\002 \001(\005\022\014\n\004data\030\003 \001(\014\022\016\n\006se" +
      "nder\030\004 \001(\005B\021\n\013proto.typesB\002rbb\006proto3"
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
          proto.types.meta.getDescriptor(),
        }, assigner);
    internal_static_proto_types_RBMsg_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_proto_types_RBMsg_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_types_RBMsg_descriptor,
        new java.lang.String[] { "M", "Type", "Data", "Sender", });
    proto.types.meta.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}