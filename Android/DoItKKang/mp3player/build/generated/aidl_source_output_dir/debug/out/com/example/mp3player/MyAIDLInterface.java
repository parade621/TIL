/*
 * This file is auto-generated.  DO NOT MODIFY.
 */
package com.example.mp3player;
// Declare any non-default types here with import statements

public interface MyAIDLInterface extends android.os.IInterface
{
  /** Default implementation for MyAIDLInterface. */
  public static class Default implements com.example.mp3player.MyAIDLInterface
  {
    /**
         * Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
    @Override public int getMaxDuration() throws android.os.RemoteException
    {
      return 0;
    }
    @Override public void start() throws android.os.RemoteException
    {
    }
    @Override public void stop() throws android.os.RemoteException
    {
    }
    @Override
    public android.os.IBinder asBinder() {
      return null;
    }
  }
  /** Local-side IPC implementation stub class. */
  public static abstract class Stub extends android.os.Binder implements com.example.mp3player.MyAIDLInterface
  {
    private static final java.lang.String DESCRIPTOR = "com.example.mp3player.MyAIDLInterface";
    /** Construct the stub at attach it to the interface. */
    public Stub()
    {
      this.attachInterface(this, DESCRIPTOR);
    }
    /**
     * Cast an IBinder object into an com.example.mp3player.MyAIDLInterface interface,
     * generating a proxy if needed.
     */
    public static com.example.mp3player.MyAIDLInterface asInterface(android.os.IBinder obj)
    {
      if ((obj==null)) {
        return null;
      }
      android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
      if (((iin!=null)&&(iin instanceof com.example.mp3player.MyAIDLInterface))) {
        return ((com.example.mp3player.MyAIDLInterface)iin);
      }
      return new com.example.mp3player.MyAIDLInterface.Stub.Proxy(obj);
    }
    @Override public android.os.IBinder asBinder()
    {
      return this;
    }
    @Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
    {
      java.lang.String descriptor = DESCRIPTOR;
      switch (code)
      {
        case INTERFACE_TRANSACTION:
        {
          reply.writeString(descriptor);
          return true;
        }
        case TRANSACTION_getMaxDuration:
        {
          data.enforceInterface(descriptor);
          int _result = this.getMaxDuration();
          reply.writeNoException();
          reply.writeInt(_result);
          return true;
        }
        case TRANSACTION_start:
        {
          data.enforceInterface(descriptor);
          this.start();
          reply.writeNoException();
          return true;
        }
        case TRANSACTION_stop:
        {
          data.enforceInterface(descriptor);
          this.stop();
          reply.writeNoException();
          return true;
        }
        default:
        {
          return super.onTransact(code, data, reply, flags);
        }
      }
    }
    private static class Proxy implements com.example.mp3player.MyAIDLInterface
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
      /**
           * Demonstrates some basic types that you can use as parameters
           * and return values in AIDL.
           */
      @Override public int getMaxDuration() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        int _result;
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_getMaxDuration, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            return getDefaultImpl().getMaxDuration();
          }
          _reply.readException();
          _result = _reply.readInt();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
        return _result;
      }
      @Override public void start() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_start, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().start();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      @Override public void stop() throws android.os.RemoteException
      {
        android.os.Parcel _data = android.os.Parcel.obtain();
        android.os.Parcel _reply = android.os.Parcel.obtain();
        try {
          _data.writeInterfaceToken(DESCRIPTOR);
          boolean _status = mRemote.transact(Stub.TRANSACTION_stop, _data, _reply, 0);
          if (!_status && getDefaultImpl() != null) {
            getDefaultImpl().stop();
            return;
          }
          _reply.readException();
        }
        finally {
          _reply.recycle();
          _data.recycle();
        }
      }
      public static com.example.mp3player.MyAIDLInterface sDefaultImpl;
    }
    static final int TRANSACTION_getMaxDuration = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    static final int TRANSACTION_start = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
    static final int TRANSACTION_stop = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    public static boolean setDefaultImpl(com.example.mp3player.MyAIDLInterface impl) {
      // Only one user of this interface can use this function
      // at a time. This is a heuristic to detect if two different
      // users in the same process use this function.
      if (Stub.Proxy.sDefaultImpl != null) {
        throw new IllegalStateException("setDefaultImpl() called twice");
      }
      if (impl != null) {
        Stub.Proxy.sDefaultImpl = impl;
        return true;
      }
      return false;
    }
    public static com.example.mp3player.MyAIDLInterface getDefaultImpl() {
      return Stub.Proxy.sDefaultImpl;
    }
  }
  /**
       * Demonstrates some basic types that you can use as parameters
       * and return values in AIDL.
       */
  public int getMaxDuration() throws android.os.RemoteException;
  public void start() throws android.os.RemoteException;
  public void stop() throws android.os.RemoteException;
}
