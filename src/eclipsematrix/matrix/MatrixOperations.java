package eclipsematrix.matrix;

import java.lang.reflect.InvocationTargetException;

import matrix.db.Context;
import matrix.util.MatrixException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;

import eclipsematrix.EclipseMatrix;

/**
 * 
 * @author Hannes Lenke hannes@lenke.at
 * 
 */
public class MatrixOperations implements IRunnableWithProgress {

	private String host;
	private String user;
	private String password;

	/**
	 * @return Returns the host.
	 */
	public String getHost() {
		return this.host;
	}

	/**
	 * @param host
	 *            The host to set.
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @param password
	 *            The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the user.
	 */
	public String getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws InvocationTargetException,
			InterruptedException {
		System.out.println("run");
		try {
			monitor.beginTask("Connecting to Matrix Host " + this.host
					+ " as user " + this.user, IProgressMonitor.UNKNOWN);
			// Context ctx = new Context(this.host);//
			Context ctx = new Context("test", this.host);
			System.out.println(ctx.toString());
			ctx.setUser(this.user);
			ctx.setPassword(this.password);
			ctx.connect();
			while (!monitor.isCanceled()) {
				monitor.worked(IProgressMonitor.UNKNOWN);
				if (ctx.isConnected()) {
					EclipseMatrix.getDefault().setContext(ctx);
					EclipseMatrix.getDefault().setHost(this.host);
					EclipseMatrix.getDefault().setUser(this.user);
					// MxJPOcheckin.triggerOnOff();
					break;
				}
			}
			monitor.done();
		} catch (MatrixException e) {
			throw new InvocationTargetException(e);
		}
	}

	public void login() throws MatrixException {
		// Context ctx = new Context(this.host);
		Context ctx = new Context("test", this.host);
		ctx.setUser(this.user);
		ctx.setPassword(this.password);
		ctx.connect();
		if (ctx.isConnected()) {
			EclipseMatrix.getDefault().setContext(ctx);
			// MxEclipseUtils.triggerOnOff();
		}
	}
}
