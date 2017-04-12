package com.allthinkit;

import java.awt.Event;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientAsync {
	protected Socket client = null;
	protected String _Host = "127.0.0.1";
	protected int _port = 80;
	protected int _timeout = 1000 * 30;// 30 Seconds.
	private byte[] _bSocketReceive = null;

	public byte[] GetByteReceive() {
		return _bSocketReceive;
	}

	private HasReceiveListener listener;

	public interface HasReceiveListener {
		void doHandleReceive();
	}

	public void SetHasReceiveListener(HasReceiveListener listener) {
		this.listener = listener;
	}

	protected boolean isConnected = false;

	// region Constructor..
	public ClientAsync(int port) {
		this._port = port;
	}

	public ClientAsync(String host, int port) {
		this._Host = host;
		this._port = port;
	}
	// endregion

	// region Properties..
	public void SetTimeout(int itimeout) {
		this._timeout = 1000 * itimeout;
	}

	public int GetTimeout() {
		return this._timeout;
	}

	public void SetHost(String strHost) {
		this._Host = strHost;
	}

	public String GetHost() {
		return this._Host;
	}

	public void SetPort(int iport) {
		this._port = iport;
	}

	public int GetPort() {
		return this._port;
	}
	// endregion

	// region Method..
	public boolean doConnect() throws UnknownHostException, IOException, Exception {
		boolean blreturn = false;

		this.client = new Socket(this._Host, this._port);
		Thread.sleep((long) (1000 * 0.5)); // 0.5 Seconds
		Thread th = new Thread() {
			public void run() {
				while (isConnected) {
					_bSocketReceive = null;
					try {
						if (client.getInputStream().available() > 0) {
							_bSocketReceive = doReceive();
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (_bSocketReceive != null) {
						// TODO :
						if (listener != null) {
							listener.doHandleReceive();
						}
					}
				}
			}
		};
		th.start();
		blreturn = this.client.isConnected();
		isConnected = blreturn;

		return blreturn;
	}

	public boolean doDisconnect() throws IOException, Exception {
		boolean blReturn = false;
		if (this.client != null) {
			if (this.client.isConnected()) {
				this.client.close();
				blReturn = true;
				isConnected = false;
			}
		}
		return blReturn;
	}

	public boolean doSend(byte[] bMsg) throws IOException, Exception {
		boolean blReturn = false;
		if (this.client.isConnected()) {
			try {
				OutputStream outStream = this.client.getOutputStream();
				outStream.flush();
				outStream.write(bMsg);
				blReturn = true;
			} catch (IOException iex) {
				throw iex;
			} catch (Exception ex) {
				throw ex;
			}

		}
		return blReturn;
	}

	private byte[] doReceive() throws IOException {
		byte[] bReturn = null;
		if (this.client != null) {
			if (this.client.isConnected()) {
				InputStream inStream = this.client.getInputStream();
				if (inStream.available() > 0) {
					int iBuffer = inStream.available();
					bReturn = new byte[iBuffer];
					inStream.read(bReturn, 0, iBuffer);
				}
			}
		}
		return bReturn;
	}
	// endregion

}
