/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/di/Androidworkspace/Assignment7_DiXiao/Assignment7_DiXiao/src/com/dixiao/enhancedcpdemo/DownloadRequest.aidl
 */
package com.dixiao.enhancedcpdemo;
public interface DownloadRequest extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.dixiao.enhancedcpdemo.DownloadRequest
{
private static final java.lang.String DESCRIPTOR = "com.dixiao.enhancedcpdemo.DownloadRequest";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.dixiao.enhancedcpdemo.DownloadRequest interface,
 * generating a proxy if needed.
 */
public static com.dixiao.enhancedcpdemo.DownloadRequest asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.dixiao.enhancedcpdemo.DownloadRequest))) {
return ((com.dixiao.enhancedcpdemo.DownloadRequest)iin);
}
return new com.dixiao.enhancedcpdemo.DownloadRequest.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_downloadImage:
{
data.enforceInterface(DESCRIPTOR);
android.net.Uri _arg0;
if ((0!=data.readInt())) {
_arg0 = android.net.Uri.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
com.dixiao.enhancedcpdemo.DownloadCallback _arg1;
_arg1 = com.dixiao.enhancedcpdemo.DownloadCallback.Stub.asInterface(data.readStrongBinder());
this.downloadImage(_arg0, _arg1);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.dixiao.enhancedcpdemo.DownloadRequest
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/** the remote method in which the image is downloaded */
@Override public void downloadImage(android.net.Uri uri, com.dixiao.enhancedcpdemo.DownloadCallback callback) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((uri!=null)) {
_data.writeInt(1);
uri.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeStrongBinder((((callback!=null))?(callback.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_downloadImage, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_downloadImage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/** the remote method in which the image is downloaded */
public void downloadImage(android.net.Uri uri, com.dixiao.enhancedcpdemo.DownloadCallback callback) throws android.os.RemoteException;
}
