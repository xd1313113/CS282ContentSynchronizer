/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/di/Androidworkspace/Assignment7_DiXiao/Assignment7_DiXiao/src/com/dixiao/enhancedcpdemo/DownloadCallback.aidl
 */
package com.dixiao.enhancedcpdemo;
public interface DownloadCallback extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.dixiao.enhancedcpdemo.DownloadCallback
{
private static final java.lang.String DESCRIPTOR = "com.dixiao.enhancedcpdemo.DownloadCallback";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.dixiao.enhancedcpdemo.DownloadCallback interface,
 * generating a proxy if needed.
 */
public static com.dixiao.enhancedcpdemo.DownloadCallback asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.dixiao.enhancedcpdemo.DownloadCallback))) {
return ((com.dixiao.enhancedcpdemo.DownloadCallback)iin);
}
return new com.dixiao.enhancedcpdemo.DownloadCallback.Stub.Proxy(obj);
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
case TRANSACTION_sendPath:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendPath(_arg0);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.dixiao.enhancedcpdemo.DownloadCallback
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
/** The callback method to send imagePath back the thread */
@Override public void sendPath(java.lang.String imageFilePath) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(imageFilePath);
mRemote.transact(Stub.TRANSACTION_sendPath, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_sendPath = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
/** The callback method to send imagePath back the thread */
public void sendPath(java.lang.String imageFilePath) throws android.os.RemoteException;
}
