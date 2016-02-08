package rkreikebaumspellchecker;

import java.io.*;
import java.util.*;

import com.cloudgarden.resource.*;

import org.apache.commons.io.*;
import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class MainWindow extends org.eclipse.swt.widgets.Composite {

	Properties appSettings = new Properties();
	Cursor defaultCursor; // To change the cursor to an arrow at any point after MainWindow() has executed, use setCursor(defaultCursor);
	Cursor waitCursor; // To change the cursor to an hourglass at any point after MainWindow() has executed, use setCursor(waitCursor);
	private Menu menu1;
	private MenuItem aboutMenuItem;
	private Menu helpMenu;
	private MenuItem helpMenuItem;
	private MenuItem exitMenuItem;
	private MenuItem closeFileMenuItem;
	private MenuItem saveFileMenuItem;
	private MenuItem newFileMenuItem;
	private MenuItem openFileMenuItem;
	private ToolItem newToolItem;
	private ToolItem saveToolItem;
	private ToolItem openToolItem;
	private ToolBar toolBar;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep2;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep1;
	private Composite clientArea;
	private Label statusText;
	private Composite statusArea;
	private Button btnAdd;
	private Button btnReplace;
	private Combo cmbReplaceWith;
	private Button btnChange;
	private Button btnIgnore;
	private Composite cmpSpellCheckBar;
	@SuppressWarnings("unused")
	private MenuItem fileMenuSep3;
	private MenuItem importDictionaryFileMenuItem;
	private Text textEditor;
	private Menu fileMenu;
	private MenuItem fileMenuItem;
	private ToolItem checkSpellingToolItem;
	private SpellChecker sc;
	private File curFile;
//	private	GridData spellCheckBarLayout;


	{
		// Register as a resource user - SWTResourceManager will handle the obtaining and disposing of resources
		SWTResourceManager.registerResourceUser(this);
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		Shell shell = new Shell(display);
		@SuppressWarnings("unused")
		MainWindow inst = new MainWindow(shell, SWT.NULL);
		shell.setLayout(new FillLayout());
		shell.setImage(SWTResourceManager.getImage("images/16x16.png"));
		shell.setText("RKreikebaumSpellChecker");
		shell.setBackgroundImage(SWTResourceManager.getImage("images/ToolbarBackground.gif"));
		shell.layout();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public MainWindow(Composite parent, int style) {
		super(parent, style);
		initGUI();
		setPreferences();
		sc = new SpellChecker();
		waitCursor = getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
		defaultCursor = getDisplay().getSystemCursor(SWT.CURSOR_ARROW);
		clientArea.setFocus();
		GridData spellCheckBarLayout = new GridData();
		spellCheckBarLayout.widthHint = 474;
		spellCheckBarLayout.heightHint = 25;
		spellCheckBarLayout.grabExcessHorizontalSpace = true;
		cmpSpellCheckBar.setLayoutData(spellCheckBarLayout);
		// I tried to have the checkSpelling method just change the heightHint of the GridData object, 
		// but it wouldn't work. No matter how hard I tried, I couldn't get the CompositeWidget to pop up 
		// without having it excluded from the layout.
		spellCheckBarLayout.exclude = true;
		cmpSpellCheckBar.setVisible(false);
	}

	// Load application settings from .ini file
	private void setPreferences() {
		try {
			appSettings.load(new FileInputStream("appsettings.ini"));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		// By default, center window
		int width = Integer.parseInt(appSettings.getProperty("width", "640"));
		int height = Integer.parseInt(appSettings.getProperty("height", "480"));
		Rectangle screenBounds = getDisplay().getBounds();
		int defaultTop = (screenBounds.height - height) / 2;
		int defaultLeft = (screenBounds.width - width) / 2;
		int top = Integer.parseInt(appSettings.getProperty("top", String.valueOf(defaultTop)));
		int left = Integer.parseInt(appSettings.getProperty("left", String.valueOf(defaultLeft)));
		getShell().setSize(width, height);
		getShell().setLocation(left, top);
		saveShellBounds();
	}

	/**
	 * Initializes the GUI.
	 */
	private void initGUI() {
		try {
			getShell().addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent evt) {
					shellWidgetDisposed(evt);
				}
			});

			getShell().addControlListener(new ControlAdapter() {
				public void controlResized(ControlEvent evt) {
					shellControlResized(evt);
				}
			});

			getShell().addControlListener(new ControlAdapter() {
				public void controlMoved(ControlEvent evt) {
					shellControlMoved(evt);
				}
			});

			GridLayout thisLayout = new GridLayout();
			this.setLayout(thisLayout);
			{
				GridData toolBarLData = new GridData();
				toolBarLData.grabExcessHorizontalSpace = true;
				toolBarLData.horizontalAlignment = GridData.FILL;
				toolBar = new ToolBar(this, SWT.FLAT);
				toolBar.setLayoutData(toolBarLData);
				toolBar.setBackgroundImage(SWTResourceManager.getImage("images/ToolbarBackground.gif"));
				{
					newToolItem = new ToolItem(toolBar, SWT.NONE);
					newToolItem.setImage(SWTResourceManager.getImage("images/new.gif"));
					newToolItem.setToolTipText("New");
				}
				{
					openToolItem = new ToolItem(toolBar, SWT.NONE);
					openToolItem.setToolTipText("Open");
					openToolItem.setImage(SWTResourceManager.getImage("images/open.gif"));
					openToolItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							openToolItemWidgetSelected(evt);
						}
					});
				}
				{
					saveToolItem = new ToolItem(toolBar, SWT.NONE);
					saveToolItem.setToolTipText("Save");
					saveToolItem.setImage(SWTResourceManager.getImage("images/save.gif"));
					saveToolItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							saveToolItemWidgetSelected(evt);
						}
					});
				}
				{
					checkSpellingToolItem = new ToolItem(toolBar, SWT.NONE);
					checkSpellingToolItem.setToolTipText("Check Spelling");
					checkSpellingToolItem.setImage(SWTResourceManager.getImage("images/spellcheck2_small.png"));
					checkSpellingToolItem.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							checkSpellingToolItemWidgetSelected(evt);
						}
					});
				}
			}
			{
				cmpSpellCheckBar = new Composite(this, SWT.NONE);
				GridData cmpSpellCheckBarLData = new GridData();
				cmpSpellCheckBarLData.widthHint = 474;
				cmpSpellCheckBarLData.heightHint = 35;
				cmpSpellCheckBarLData.grabExcessHorizontalSpace = true;
				cmpSpellCheckBar.setLayoutData(cmpSpellCheckBarLData);
				cmpSpellCheckBar.setLayout(null);
				{
					btnIgnore = new Button(cmpSpellCheckBar, SWT.PUSH | SWT.CENTER);
					btnIgnore.setText("Ignore");
					btnIgnore.setBounds(6, 7, 60, 23);
					btnIgnore.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							btnIgnoreWidgetSelected(evt);
						}
					});
				}

				{
					btnChange = new Button(cmpSpellCheckBar, SWT.PUSH | SWT.CENTER);
					btnChange.setText("Change");
					btnChange.setBounds(72, 7, 60, 23);
					btnChange.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							btnChangeWidgetSelected(evt);
						}
					});
				}
				{
					btnAdd = new Button(cmpSpellCheckBar, SWT.PUSH | SWT.CENTER);
					btnAdd.setText("Add to Dictionary");
					btnAdd.setBounds(138, 7, 116, 23);
					btnAdd.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							btnAddWidgetSelected(evt);
						}
					});
				}
				{
					cmbReplaceWith = new Combo(cmpSpellCheckBar, SWT.NONE);
					cmbReplaceWith.setText("Replace With");
					cmbReplaceWith.setBounds(260, 7, 122, 23);
				}
				{
					btnReplace = new Button(cmpSpellCheckBar, SWT.PUSH | SWT.CENTER);
					btnReplace.setText("Replace");
					btnReplace.setSize(60, 30);
					btnReplace.setBounds(388, 7, 60, 23);
					btnReplace.addSelectionListener(new SelectionAdapter() {
						public void widgetSelected(SelectionEvent evt) {
							btnReplaceWidgetSelected(evt);
						}
					});
				}
			}
			{
				clientArea = new Composite(this, SWT.NONE);
				FillLayout clientAreaLayout = new FillLayout(org.eclipse.swt.SWT.HORIZONTAL);
				GridData clientAreaLData = new GridData();
				clientAreaLData.grabExcessHorizontalSpace = true;
				clientAreaLData.grabExcessVerticalSpace = true;
				clientAreaLData.horizontalAlignment = GridData.FILL;
				clientAreaLData.verticalAlignment = GridData.FILL;
				clientArea.setLayoutData(clientAreaLData);
				clientArea.setLayout(clientAreaLayout);
				{
					textEditor = new Text(clientArea, SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
					textEditor.setBounds(0, 0, 60, 30);
				}
			}
			{
				statusArea = new Composite(this, SWT.NONE);
				GridLayout statusAreaLayout = new GridLayout();
				statusAreaLayout.makeColumnsEqualWidth = true;
				statusAreaLayout.horizontalSpacing = 0;
				statusAreaLayout.marginHeight = 0;
				statusAreaLayout.marginWidth = 0;
				statusAreaLayout.verticalSpacing = 0;
				statusAreaLayout.marginLeft = 3;
				statusAreaLayout.marginRight = 3;
				statusAreaLayout.marginTop = 3;
				statusAreaLayout.marginBottom = 3;
				statusArea.setLayout(statusAreaLayout);
				GridData statusAreaLData = new GridData();
				statusAreaLData.horizontalAlignment = GridData.FILL;				
				statusAreaLData.grabExcessHorizontalSpace = true;				
				statusArea.setLayoutData(statusAreaLData);
				statusArea.setBackground(SWTResourceManager.getColor(239, 237, 224));
				{
					statusText = new Label(statusArea, SWT.BORDER);
					statusText.setText(" Ready");
					GridData txtStatusLData = new GridData();
					txtStatusLData.horizontalAlignment = GridData.FILL;
					txtStatusLData.grabExcessHorizontalSpace = true;
					txtStatusLData.verticalIndent = 3;
					statusText.setLayoutData(txtStatusLData);
				}
			}
			thisLayout.verticalSpacing = 0;
			thisLayout.marginWidth = 0;
			thisLayout.marginHeight = 0;
			thisLayout.horizontalSpacing = 0;
			thisLayout.marginTop = 3;
			this.setSize(474, 312);
			{
				menu1 = new Menu(getShell(), SWT.BAR);
				getShell().setMenuBar(menu1);
				{
					fileMenuItem = new MenuItem(menu1, SWT.CASCADE);
					fileMenuItem.setText("&File");
					{
						fileMenu = new Menu(fileMenuItem);
						{
							newFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							newFileMenuItem.setText("&New");
							newFileMenuItem.setImage(SWTResourceManager.getImage("images/new.gif"));
						}
						{
							openFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							openFileMenuItem.setText("&Open");
							openFileMenuItem.setImage(SWTResourceManager.getImage("images/open.gif"));
							openFileMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									openFileMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							closeFileMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							closeFileMenuItem.setText("Close");
						}
						{
							fileMenuSep1 = new MenuItem(fileMenu, SWT.SEPARATOR);
						}
						{
							importDictionaryFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							importDictionaryFileMenuItem.setText("Import Dictionary");
							importDictionaryFileMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									importDictionaryFileMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							fileMenuSep2 = new MenuItem(fileMenu, SWT.SEPARATOR);
						}
						{
							saveFileMenuItem = new MenuItem(fileMenu, SWT.PUSH);
							saveFileMenuItem.setText("&Save");
							saveFileMenuItem.setImage(SWTResourceManager.getImage("images/save.gif"));
							saveFileMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									saveFileMenuItemWidgetSelected(evt);
								}
							});
						}
						{
							fileMenuSep3 = new MenuItem(fileMenu, SWT.SEPARATOR);
						}
						{
							exitMenuItem = new MenuItem(fileMenu, SWT.CASCADE);
							exitMenuItem.setText("E&xit");
							exitMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									exitMenuItemWidgetSelected(evt);
								}
							});
						}
						fileMenuItem.setMenu(fileMenu);
					}
				}
				{
					helpMenuItem = new MenuItem(menu1, SWT.CASCADE);
					helpMenuItem.setText("&Help");
					{
						helpMenu = new Menu(helpMenuItem);
						{
							aboutMenuItem = new MenuItem(helpMenu, SWT.CASCADE);
							aboutMenuItem.setText("&About");
							aboutMenuItem.addSelectionListener(new SelectionAdapter() {
								public void widgetSelected(SelectionEvent evt) {
									aboutMenuItemWidgetSelected(evt);
								}
							});
						}
						helpMenuItem.setMenu(helpMenu);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void exitMenuItemWidgetSelected(SelectionEvent evt) {
		File dictFile = new File("my_Dictionary.txt");	
		String saveError;

		if (sc.dictionaryHasChanged() || !dictFile.exists()) {
			try {
				dictFile.createNewFile();
				setStatus("Saving " + dictFile.getName());
				saveError = sc.saveDictionary(dictFile);
				if (saveError == null) {
					setStatus(" Dictionary Saved");
				}
				else {
					setStatus(saveError);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				setStatus(" IOException: could not create file " + dictFile.getName());
			}
		}
		
		try {
			// Save app settings to file
			appSettings.store(new FileOutputStream("appsettings.ini"), "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
		getShell().dispose();
	}

	private void openFileMenuItemWidgetSelected(SelectionEvent evt) {
		String[] exts = new String[1];
		exts[0] = "*.txt";
		
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		dialog.setFilterExtensions(exts);
		dialog.setFilterIndex(0);
		String filename = dialog.open();
		String textString = null;

		if (filename != null) {
			getShell().setText(filename);
			curFile = new File(filename);
			try {
				textString = FileUtils.readFileToString(curFile);
			} catch (IOException e) {
				e.printStackTrace();
				setStatus(" IO Exception");
			}
			if (textString != null)
			{
				textEditor.setText(textString);
			}
		}

	}

	private void openToolItemWidgetSelected(SelectionEvent evt) {
		openFileMenuItemWidgetSelected(evt);
	}

	private void aboutMenuItemWidgetSelected(SelectionEvent evt) {
		MessageBox message = new MessageBox(getShell(), SWT.OK | SWT.ICON_INFORMATION);
		message.setText("About RKreikebaumSpellChecker");
		message.setMessage("This is Rick Kreikebaum's awesome spellchecker. \n\nRKreikebaumSpellChecker v1.0");
		message.open();
	}

	private void shellWidgetDisposed(DisposeEvent evt) {
		try {
			// Save app settings to file
			appSettings.store(new FileOutputStream("appsettings.ini"), "");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private void shellControlResized(ControlEvent evt) {
		saveShellBounds();
	}

	// Save window location in appSettings hash table
	private void saveShellBounds() {
		// Save window bounds in app settings
		Rectangle bounds = getShell().getBounds();
		appSettings.setProperty("top", String.valueOf(bounds.y));
		appSettings.setProperty("left", String.valueOf(bounds.x));
		appSettings.setProperty("width", String.valueOf(bounds.width));
		appSettings.setProperty("height", String.valueOf(bounds.height));
	}

	private void shellControlMoved(ControlEvent evt) {
		saveShellBounds();
	}

	private void setStatus(String message) {
		statusText.setText(message);
	}
	
	private void saveFileMenuItemWidgetSelected(SelectionEvent evt) {
		String[] exts = new String[1];
		exts[0] = "*.txt";
		String fileName;
				
		if (curFile == null)
		{
			FileDialog dialog = new FileDialog(getShell(), SWT.SAVE);
			dialog.setFilterExtensions(exts);
			dialog.setFilterIndex(0);
			
			fileName = dialog.open();
			if (fileName != null) {
				curFile = new File(fileName);
				getShell().setText(fileName);
				
				try {
					FileUtils.writeStringToFile(curFile, textEditor.getText());
				} catch (IOException e) {
					//e.printStackTrace();
					setStatus(" IO Exception");
				}
			}
		}
		else
		{
			try {
				FileUtils.writeStringToFile(curFile, textEditor.getText());
			} catch (IOException e) {
				//e.printStackTrace();
				setStatus(" IO Exception");
			}			
		}
		setStatus(" Saved");
	}
	
	private void saveToolItemWidgetSelected(SelectionEvent evt) {
		saveFileMenuItemWidgetSelected(evt);
	}
	
	private void checkSpellingToolItemWidgetSelected(SelectionEvent evt) {
		String[] exts = new String[1];
		exts[0] = "*.txt";
		
		File dictFile = new File("my_dictionary.txt");
		if (sc.dictionaryIsEmpty()) {
			if (dictFile.exists()) {
				setStatus(" Loading dictionary...");
				sc.loadDictionary(dictFile);
				setStatus(" Dictionary " + dictFile.getName() + " imported.");			
			}
			else
			{
				setStatus(" Dictionary " + dictFile.getName() + " not found. Please select a dictionary to import.");
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setText("Import Dictionary");
				dialog.setFilterExtensions(exts);
				dialog.setFilterIndex(0);
				String filename = dialog.open();

				if (filename != null) {
					dictFile = new File(filename);
					setStatus(" Loading dictionary...");
					sc.loadDictionary(dictFile);
					setStatus(" Dictionary " + dictFile.getName() + " imported.");				
				}
			}
		}
		
//		textEditor.setEditable(false);
		textEditor.setEnabled(false);
		sc.loadDocument(textEditor.getText());

		// Do spell-checking magic here
		setStatus(" Spell checking in progress...");
		if (sc.findProblem()) {
			cmpSpellCheckBar.setVisible(true);
			textEditor.setSelection(sc.getWordStart(), sc.getWordEnd());
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			cmbReplaceWith.add(sc.getPreceding(), 0);
			cmbReplaceWith.add(sc.getSucceeding(), 1);
		}
		else {
			cmpSpellCheckBar.setVisible(false);
			setStatus(" Spell checking finished!");
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			textEditor.clearSelection();
//			textEditor.setEditable(true);
			textEditor.setEnabled(true);
		}
		
	}
	
	private void importDictionaryFileMenuItemWidgetSelected(SelectionEvent evt) {
		String[] exts = new String[1];
		exts[0] = "*.txt";

		setStatus(" Select a dictionary to import.");
		File dictFile;
		FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
		dialog.setText("Import Dictionary");
		dialog.setFilterExtensions(exts);
		dialog.setFilterIndex(0);
		String filename = dialog.open();

		if (filename != null) {
			setStatus(" Loading dictionary...");
			dictFile = new File(filename);
			sc.loadDictionary(dictFile);
			setStatus(" Dictionary " + dictFile.getName() + " imported.");
		}
	}
	
	private void btnIgnoreWidgetSelected(SelectionEvent evt) {
		if (sc.findProblem()) {
			cmpSpellCheckBar.setVisible(true);
			textEditor.setSelection(sc.getWordStart(), sc.getWordEnd());
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			cmbReplaceWith.add(sc.getPreceding(), 0);
			cmbReplaceWith.add(sc.getSucceeding(), 1);
		}
		else {
			cmpSpellCheckBar.setVisible(false);
			setStatus(" Spell checking finished!");
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			textEditor.clearSelection();
			textEditor.setEnabled(true);
		}
	}
	
	private void btnChangeWidgetSelected(SelectionEvent evt) {
		InputDialog diag = new InputDialog(getShell(), "Change Word", "Replace with the following word:", sc.getWord(), null);
		diag.open();
		String word = diag.getValue();
		if (word != null) {
			textEditor.insert(word);
			sc.adjustOffsetByWdLen(word.length());
		}
		if (sc.findProblem()) {
			cmpSpellCheckBar.setVisible(true);
			textEditor.setSelection(sc.getWordStart(), sc.getWordEnd());
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			cmbReplaceWith.add(sc.getPreceding(), 0);
			cmbReplaceWith.add(sc.getSucceeding(), 1);
		}
		else {
			cmpSpellCheckBar.setVisible(false);
			setStatus(" Spell checking finished!");
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			textEditor.clearSelection();
			textEditor.setEnabled(true);
		}
	}
	
	private void btnReplaceWidgetSelected(SelectionEvent evt) {
		String word = null;
		if (cmbReplaceWith.getSelectionIndex() >= 0) {
			word = cmbReplaceWith.getItem(cmbReplaceWith.getSelectionIndex());
			textEditor.insert(word);
			sc.adjustOffsetByWdLen(word.length());
		}	
		if (sc.findProblem()) {
			cmpSpellCheckBar.setVisible(true);
			textEditor.setSelection(sc.getWordStart(), sc.getWordEnd());
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			cmbReplaceWith.add(sc.getPreceding(), 0);
			cmbReplaceWith.add(sc.getSucceeding(), 1);
		}
		else {
			cmpSpellCheckBar.setVisible(false);
			setStatus(" Spell checking finished!");
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			textEditor.clearSelection();
			textEditor.setEnabled(true);
		}
	}
	
	private void btnAddWidgetSelected(SelectionEvent evt) {
		sc.addToDictionary(sc.getWord());
		if (sc.findProblem()) {
			cmpSpellCheckBar.setVisible(true);
			textEditor.setSelection(sc.getWordStart(), sc.getWordEnd());
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			cmbReplaceWith.add(sc.getPreceding(), 0);
			cmbReplaceWith.add(sc.getSucceeding(), 1);
		}
		else {
			cmpSpellCheckBar.setVisible(false);
			setStatus(" Spell checking finished!");
			cmbReplaceWith.removeAll();
			cmbReplaceWith.setText("Replace With");
			textEditor.clearSelection();
			textEditor.setEnabled(true);
		}
	}
}
