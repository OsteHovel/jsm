package com.ostsoft.games.jsm.util;

public class ByteStream {
    private int position = 0;
    private byte[] buffer;

    public ByteStream(byte[] buffer) {
        this.buffer = buffer;
    }

    public byte read() {
        return buffer[position++];
    }

    public void read(byte[] bytes) {
        System.arraycopy(buffer, position, bytes, 0, bytes.length);
        position += bytes.length;
    }

    public void read(byte[] bytes, int offset, int length) {
        System.arraycopy(buffer, position, bytes, offset, length);
        position += length;
    }

    public int readUnsignedByte() {
        return Byte.toUnsignedInt(buffer[position++]);
    }

    public int readReversedUnsignedShort() {
        return Byte.toUnsignedInt(buffer[position++]) | (Byte.toUnsignedInt(buffer[position++]) << 8);
    }

    public int getPosition() {
        return position;
    }

    public ByteStream setPosition(int position) {
        if (position >= buffer.length) {
            throw new IndexOutOfBoundsException("Position is exceeding the byteStream (Position:" + position + " Size: " + buffer.length + ")");
        }
        this.position = position;
        return this;
    }

    public ByteStream seek(int length) {
        position += length;
        return this;
    }

    public byte readByte() {
        return buffer[position++];
    }

    public int readReversedUnsigned3Bytes() {
        return Byte.toUnsignedInt(buffer[position++]) | (Byte.toUnsignedInt(buffer[position++]) << 8) | (Byte.toUnsignedInt(buffer[position++]) << 16);
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public ByteStream writeUnsignedByte(int unsignedByte) {
        buffer[position++] = unsignedIntToSignedByte(unsignedByte);
        return this;
    }

    public ByteStream writeUnsignedReversedShort(int i) {
        buffer[position++] = (byte) (i & 0xFF);
        buffer[position++] = (byte) ((i >> 8) & 0xFF);
        return this;
    }

    public ByteStream writeUnsignedReversed3Bytes(int i) {
        buffer[position++] = (byte) (i & 0xFF);
        buffer[position++] = (byte) ((i >> 8) & 0xFF);
        buffer[position++] = (byte) ((i >> 16) & 0xFF);
        return this;
    }

    public int size() {
        return buffer.length;
    }

    private byte unsignedIntToSignedByte(int signedInt) {
        return (byte) (signedInt & 0xFF);
    }

    public void writeBytes(byte[] bytes) {
        for (byte aByte : bytes) {
            buffer[position++] = aByte;
        }
    }

    public void writeBytes(byte[] bytes, int length) {
        System.arraycopy(bytes, 0, buffer, position, length);
        position += length;
    }

    public String readString(int numberOfChar) {
        char[] chars = new char[numberOfChar];
        for (int i = 0; i < numberOfChar; i++) {
            chars[i] = (char) readUnsignedByte();
        }
        return new String(chars).trim();
    }

    public void writeString(String string) {
        byte[] bytes = new byte[string.length()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) string.charAt(i);
        }
        writeBytes(bytes);
    }
}
