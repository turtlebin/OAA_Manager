package gri.manager.ui.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

public class ImageViewerDialog extends Dialog {

	private Object result;
	protected Shell shell;
	private Shell parentShell;
	private Label label;

	private String imgPath;
	private Image image;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public ImageViewerDialog(String imagePath, Shell parent, int style) {
		super(parent, style);
		this.parentShell = parent;
		this.imgPath = imagePath;
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open() {
		Display display = getParent().getDisplay();
		createContents();
		image = new Image(display, this.imgPath);
		shell.open();

		label = new Label(shell, SWT.NONE);
		label.setBounds(0, 0, 590, 446);
		label.addPaintListener(new PaintListener() {
			@Override
			public void paintControl(PaintEvent e) {
				final Rectangle bounds = image.getBounds();
				int picwidth = bounds.width; // 图片宽
				int picheight = bounds.height; // 图片高

				Point size = shell.getSize();
				int width = size.x;// 长
				int height = size.y;// 宽
				double W = width - 30; // label的宽
				double H = height - 50; // label的高

				double ratio = 1; // 缩放比率
				double r2 = W / picwidth;
				double r1 = H / picheight;

				ratio = Math.min(r1, r2);

				e.gc.drawImage(image, 0, 0, picwidth, picheight, 0, 0, (int) (picwidth * ratio),
						(int) (picheight * ratio));
				label.setBounds(10, 10, (int) W, (int) H);
			}
		});
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		image.dispose();
		return result;

	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.MIN);
		shell.setMinimumSize(new Point(150, 50));
		shell.setText(this.imgPath);
		shell.setSize(596, 474);

		Rectangle parentBounds = parentShell.getBounds();// 父窗口居中
		Rectangle shellBounds = shell.getBounds();
		shell.setLocation(parentBounds.x + (parentBounds.width - shellBounds.width) / 2,
				parentBounds.y + (parentBounds.height - shellBounds.height) / 2);

	}
}
