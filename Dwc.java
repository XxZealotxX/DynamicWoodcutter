import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.rsbot.Configuration;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.event.listeners.MessageListener;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.gui.AccountManager;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Bank;
import org.rsbot.script.methods.Environment;
import org.rsbot.script.methods.Game.Tab;
import org.rsbot.script.methods.GrandExchange;
import org.rsbot.script.methods.Magic;
import org.rsbot.script.methods.Magic.Spell;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.util.Timer;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPath;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSWeb;

@ScriptManifest(website = "http://goo.gl/WEQX6", authors = { "hlunnb" }, keywords = { "Woodcutting, Firemaking" },
        name = "Dynamic Woodcutter", version = 1.85,
        description = "Independently trains Woodcutting and Firemaking from a low level.")
public class Dwc extends Script implements PaintListener, MouseListener, MouseMotionListener, MessageListener {
	final RSArea adviserHouse = new RSArea(new RSTile(3229, 3236), new RSTile(3232, 3241));
	final RSArea bobsArea = new RSArea(new RSTile(3227, 3201), new RSTile(3233, 3205));
	final RSTile bobsTile = new RSTile(3230, 3203);
	final RSTile treeTile = new RSTile(3172, 3224);
	final RSTile oakTreeTile = new RSTile(3101, 3286); // North East
	final RSTile oakTreeTile2 = new RSTile(3115, 3245); // East
	final RSTile oakTreeTile3 = new RSTile(3082, 3298); // North
	final RSTile willowTreeTile = new RSTile(2971, 3195); // Rimm
	final RSTile willowTreeTile2 = new RSTile(3165, 3272); // Lumb
	final RSTile willowTreeTile3 = new RSTile(3060, 3254); // Port Sarim
	final RSTile willowTreeTile4 = new RSTile(3086, 3233); // Dray
	final RSTile yewTreeTile = new RSTile(2930, 3229);// Rimm
	final RSTile yewTreeTile2 = new RSTile(3166, 3234); // Lumb
	final RSTile yewTreeTile3 = new RSTile(3048, 3270); // Port Sarim
	final RSArea generalStoreArea = new RSArea(new RSTile(3210, 3238), new RSTile(3219, 3246));
	final RSTile generalStoreTile = new RSTile(3215, 3243);
	final RSArea generalStoreArea2 = new RSArea(new RSTile(2946, 3211), new RSTile(2950, 3218));
	final RSTile generalStoreTile2 = new RSTile(2948, 3215);
	final RSArea draynorBankArea = new RSArea(new RSTile(3092, 3240), new RSTile(3097, 3246));
	final RSTile draynorBankTile = new RSTile(3093, 3244);
	final RSTile[] toGe = { new RSTile(3095, 3261), new RSTile(3094, 3266), new RSTile(3093, 3272),
	        new RSTile(3095, 3278), new RSTile(3095, 3283), new RSTile(3095, 3288), new RSTile(3089, 3291),
	        new RSTile(3089, 3294), new RSTile(3095, 3300), new RSTile(3098, 3304), new RSTile(3098, 3309),
	        new RSTile(3102, 3311), new RSTile(3108, 3311), new RSTile(3114, 3312), new RSTile(3118, 3315),
	        new RSTile(3123, 3317), new RSTile(3126, 3319), new RSTile(3131, 3324), new RSTile(3135, 3329),
	        new RSTile(3140, 3333), new RSTile(3144, 3333), new RSTile(3146, 3337), new RSTile(3146, 3342),
	        new RSTile(3145, 3347), new RSTile(3143, 3351), new RSTile(3141, 3356), new RSTile(3143, 3362),
	        new RSTile(3140, 3367), new RSTile(3139, 3372), new RSTile(3136, 3377) };
	final RSTile[] toGe2 = { new RSTile(3008, 3273), new RSTile(3008, 3274), new RSTile(3008, 3277),
	        new RSTile(3008, 3280), new RSTile(3008, 3284), new RSTile(3007, 3287), new RSTile(3007, 3291),
	        new RSTile(3007, 3295), new RSTile(3006, 3298), new RSTile(3006, 3301), new RSTile(3006, 3305),
	        new RSTile(3010, 3310), new RSTile(3010, 3312), new RSTile(3015, 3317), new RSTile(3018, 3317),
	        new RSTile(3021, 3320), new RSTile(3025, 3320), new RSTile(3029, 3320), new RSTile(3032, 3320),
	        new RSTile(3035, 3319), new RSTile(3038, 3320), new RSTile(3043, 3322), new RSTile(3046, 3322),
	        new RSTile(3049, 3322), new RSTile(3053, 3323), new RSTile(3058, 3321), new RSTile(3061, 3321),
	        new RSTile(3066, 3325), new RSTile(3067, 3330), new RSTile(3070, 3333), new RSTile(3072, 3337),
	        new RSTile(3072, 3340), new RSTile(3072, 3344), new RSTile(3072, 3348), new RSTile(3072, 3350),
	        new RSTile(3072, 3354), new RSTile(3072, 3357), new RSTile(3072, 3358), new RSTile(3072, 3363),
	        new RSTile(3072, 3368), new RSTile(3073, 3373), new RSTile(3073, 3377) };
	final RSTile[] willowsToBank = { new RSTile(2968, 3198), new RSTile(2972, 3199), new RSTile(2975, 3202),
	        new RSTile(2978, 3205), new RSTile(2982, 3206), new RSTile(2985, 3209), new RSTile(2989, 3211),
	        new RSTile(2992, 3214), new RSTile(2995, 3217), new RSTile(2998, 3220), new RSTile(3000, 3224),
	        new RSTile(3002, 3228), new RSTile(3003, 3232), new RSTile(3004, 3236), new RSTile(3006, 3240),
	        new RSTile(3010, 3242), new RSTile(3014, 3242), new RSTile(3018, 3242), new RSTile(3022, 3243),
	        new RSTile(3026, 3242), new RSTile(3030, 3242), new RSTile(3034, 3244), new RSTile(3036, 3248),
	        new RSTile(3039, 3251), new RSTile(3041, 3255), new RSTile(3044, 3259), new RSTile(3046, 3263),
	        new RSTile(3049, 3266), new RSTile(3053, 3268), new RSTile(3057, 3270), new RSTile(3060, 3273),
	        new RSTile(3064, 3274), new RSTile(3068, 3276), new RSTile(3072, 3275), new RSTile(3075, 3272),
	        new RSTile(3076, 3268), new RSTile(3076, 3264), new RSTile(3077, 3260), new RSTile(3079, 3256),
	        new RSTile(3082, 3253), new RSTile(3086, 3251), new RSTile(3089, 3248), new RSTile(3093, 3247),
	        new RSTile(3094, 3243) };
	RSPath willowsToBankPath;
	RSPath bankToWillowsPath;
	RSPath pathToGE;
	RSPath pathToGE2;
	final RSTile GETile = new RSTile(3153, 3480);
	final RSArea grandExchangeArea = new RSArea(new RSTile(3144, 3473), new RSTile(3155, 3483));
	public static final int bronzeHatchetID = 1351;
	public static final int ironHatchetID = 1349;
	public static final int steelHatchetID = 1353;
	public static final int blackHatchetID = 1361;
	public static final int mithrilHatchetID = 1355;
	public static final int adamantHatchetID = 1357;
	public static final int runeHatchetID = 1359;
	public static final int tinderboxID = 590;
	public static final int regularLogID = 1511;
	public static final int oakLogID = 1521;
	public static final int willowLogID = 1519;
	public static final int yewLogID = 1515;
	public static final int notedOakLogs = 1522;
	final int[] hatchetIDs = { runeHatchetID, adamantHatchetID, mithrilHatchetID, blackHatchetID, steelHatchetID,
	        ironHatchetID, bronzeHatchetID };
	final int[] moveIDs = { bronzeHatchetID, ironHatchetID, steelHatchetID, blackHatchetID, mithrilHatchetID,
	        adamantHatchetID, runeHatchetID, 995, tinderboxID };
	final int[] logIDs = { regularLogID, oakLogID, willowLogID, yewLogID };
	final int[] dontDropIDs = { regularLogID, oakLogID, willowLogID, yewLogID, 995, bronzeHatchetID, ironHatchetID,
	        steelHatchetID, blackHatchetID, mithrilHatchetID, adamantHatchetID, runeHatchetID, notedOakLogs, 13439,
	        tinderboxID, 14664 }; // Random event lamp 14664, Starter lamp 13439
	final int[] dontDepositIDs = { bronzeHatchetID, ironHatchetID, steelHatchetID, blackHatchetID, mithrilHatchetID,
	        adamantHatchetID, runeHatchetID, 14664, tinderboxID };
	final RSTile[] logStart = { new RSTile(3199, 3243), new RSTile(3199, 3244), new RSTile(3199, 3245),
	        new RSTile(3199, 3246), new RSTile(3196, 3237), new RSTile(3202, 3236) };
	final RSTile[] oakStart = { new RSTile(3093, 3288), new RSTile(3093, 3290), new RSTile(3093, 3289),
	        new RSTile(3106, 3273), new RSTile(3118, 3263), new RSTile(3105, 3249), new RSTile(3105, 3250),
	        new RSTile(3101, 3248) };
	final RSTile[] willowRimmStart = { new RSTile(2968, 3194), new RSTile(2968, 3193), new RSTile(2976, 3192),
	        new RSTile(2968, 3199), new RSTile(2968, 3200), new RSTile(2960, 3199) };
	final RSTile[] willowLumbStart = { new RSTile(3199, 3243), new RSTile(3199, 3244), new RSTile(3199, 3245),
	        new RSTile(3199, 3246), new RSTile(3183, 3275), new RSTile(3183, 3276) };
	final RSTile[] willowPortStart = { new RSTile(3061, 3253), new RSTile(3061, 3254), new RSTile(3068, 3274),
	        new RSTile(3068, 3275), new RSTile(3068, 3276) };
	final RSTile[] willowDrayStart = { new RSTile(3113, 3239), new RSTile(3108, 3238), new RSTile(3101, 3248),
	        new RSTile(3105, 3249), new RSTile(3105, 3250), new RSTile(3112, 3251) };
	RSTile[] start;
	final int[] treeID = { 38782, 38783, 38784, 38785, 38786, 38787, 38788, 38760 };
	final int[] oakID = { 38731, 38732 };
	final int[] willowID = { 38616, 38627, 38616, 58006 };
	final int[] yewID = { 38755 };
	int runeHatchetPrice = -1;
	int oakPrice = -1;
	int totalCash = 0;
	int bankCash = 0;
	int oakCash = 0;
	int run = random(5, 30);
	boolean checkBank = false;
	boolean useBank = false;
	boolean useDeposit = false;
	boolean needTutoring = false;
	String status = "";
	String antiBan = "";
	boolean offeredOaks = false;
	boolean sentOffer = false;
	boolean sawLamp = false;
	boolean burnLogs = false;
	boolean trainFM = false;
	boolean trainFMHovered = false;
	RSItem dontClick = null;
	RSTile dest = null;
	boolean end = false;
	boolean showPaint = false;
	boolean checkedGE = false;
	long startTime;
	long antibanTimer = 0;
	long timer2;
	long clickTimer = 1000000000000000l;
	int initialXP = -1;
	int initialXP2 = -1;
	int initialLevel = -1;
	int initialLevel2 = -1;
	boolean wasLoggedOut = false;
	double scriptVersion = Dwc.class.getAnnotation(ScriptManifest.class).version();
	double currVer = -1;
	boolean loading = false;
	final Timer dotTimer = new Timer(200);
	Timer screenshotTimer;
	final Timer dropTimer = new Timer(1300);
	Timer rpTimer = null;
	boolean wait = true;
	int bankFails = 0;
	int treeFails = 0;
	boolean dropLogs;
	boolean scriptRunning = true;
	boolean moving = false;
	Paint mp;
	private final static int[] dropPath1 = { 1, 2, 3, 4, 8, 7, 6, 5, 9, 10, 11, 12, 16, 15, 14, 13, 17, 18, 19, 20, 24,
	        23, 22, 21, 25, 26, 27, 28 };
	private final static int[] dropPath2 = { 1, 5, 2, 9, 6, 3, 13, 10, 7, 4, 17, 14, 11, 8, 21, 18, 15, 12, 25, 22, 19,
	        16, 26, 23, 20, 27, 24, 28 };
	private final static int[] dropPath3 = { 1, 5, 9, 13, 17, 21, 25, 2, 6, 10, 14, 18, 22, 26, 3, 7, 11, 15, 19, 23,
	        27, 4, 8, 12, 16, 20, 24, 28 };
	private final static int[] dropPath4 = { 4, 3, 2, 1, 5, 6, 7, 8, 12, 11, 10, 9, 13, 14, 15, 16, 20, 19, 18, 17, 21,
	        22, 23, 24, 28, 27, 26, 25 };
	private final static int[] dropPath5 = { 1, 2, 6, 5, 9, 10, 14, 13, 17, 18, 22, 21, 25, 26, 3, 4, 8, 7, 11, 12, 16,
	        15, 19, 20, 24, 23, 27, 28 };

	private enum State {
		BUYHATCHET, TREE, OAK, WILLOWRIMM, WILLOWLUMB, WILLOWPORT, WILLOWDRAY, BUYRUNEHATCHET, CHECKBANK, DEPOSIT,
		GETUTOR, LAMPS, BURN, BUYTINDERBOX, YEW, error
	}
	@Override
	public boolean onStart() {
		mouse.setSpeed(6);
		new PaintUpdater();
		mp = new Paint();
		return true;
	}
	@Override
	public void onFinish() {
		env.enableRandom("Improved Login");
		scriptRunning = false;
		log(Color.BLUE, "Thanks for using my script, please leave your feedback on my thread.");
	}
	public int loop() {
		try {
			final int check = checkStuff();
			if (check != -1)
				return check;
		} catch (Exception e) {
			log(e);
		}
		switch (getState()) {
			case BUYTINDERBOX:
				RSItem coins = inventory.getItem(995);
				if (store.isOpen()) {
					if (interfaces.getComponent(620, 20).isValid()
					        && interfaces.getComponent(620, 20).containsText("Rimmington")) {
						store.buy(tinderboxID, 1);
						return 1000;
					}
					if (interfaces.get(620).isValid()) {
						interfaces.get(620).getComponent(26).getComponent(0).interact("Take 1");
						return 1000;
					}
				}
				if (inventory.isFull()) {
					final RSItem[] inv = inventory.getItems();
					outer: for (final RSItem r : inv)
						for (final int i : logIDs)
							if (r.getID() == i) {
								inventory.dropItem(r);
								break outer;
							}
				}
				RSTile dest = calc.distanceTo(generalStoreTile) < calc.distanceTo(generalStoreTile2) ? dest = generalStoreTile
				        : generalStoreTile2;
				if (coins == null || dest.equals(generalStoreTile)) {
					if (calc.distanceTo(generalStoreTile) > 100) {
						if (!isAnimated())
							magic.castSpell(Magic.SPELL_HOME_TELEPORT);
						return 2000;
					}
					if (!generalStoreArea.contains(myLocation())) {
						webWalk(generalStoreTile);
						return random(500, 1000);
					} else {
						final RSNPC shopKeeper = npcs.getNearest(521, 520);
						if (shopKeeper != null)
							if (shopKeeper.isOnScreen()) {
								if (!shopKeeper.interact("Trade Shop"))
									return 0;
								chill();
							} else
								new Camera(shopKeeper);
					}
				}
				if (dest.equals(generalStoreTile2)) {
					if (!generalStoreArea2.contains(myLocation())) {
						webWalk(generalStoreTile2);
						return random(500, 1000);
					}
					final RSNPC shopKeeper = npcs.getNearest(530, 531);
					if (shopKeeper != null)
						if (shopKeeper.isOnScreen()) {
							if (!shopKeeper.interact("Trade Shop"))
								return 0;
							chill();
						} else
							new Camera(shopKeeper);
				}
				break;
			case BURN:
				if (getMyPlayer().isInCombat()
				        || inventory.getCount(regularLogID, oakLogID, willowLogID, yewLogID) == 0 && isIdle())
					burnLogs = false;
				if (this.dest != null)
					return walkDest();
				if (!isSelected()) {
					if (clickLog())
						return random(50, 100);
					return 0;
				}
				if (isTileFree(myLocation())) {
					burnLog();
					if (inventory.getCount(regularLogID, oakLogID, willowLogID, yewLogID) == 1)
						return random(1600, 1700);
				} else if (isIdle())
					if (inventory.getCount(regularLogID, oakLogID, willowLogID, yewLogID) < 4
					        || !findNewTile(start, false))
						burnLogs = false;
				if (antibanTimer < System.currentTimeMillis()) {
					new Camera(1000, 2000);
					antibanTimer = System.currentTimeMillis() + random(10000, 25000);
				}
				break;
			case GETUTOR:
				if (interfaces.canContinue()) {
					if (interfaces.get(241).getComponent(4).containsText("Good luck"))
						needTutoring = false;
					interfaces.clickContinue();
					return random(1000, 2000);
				}
				if (interfaces.get(230).isValid()) {
					interfaces.get(230).getComponent(2).doClick();
					return random(500, 1000);
				}
				final RSNPC tutor = npcs.getNearest(6521);
				if (tutor.isOnScreen()) {
					if (tutor.interact("Talk-to"))
						chill();
					return 0;
				} else {
					walking.walkTileMM(tutor.getLocation());
					return random(500, 1000);
				}
			case CHECKBANK:
				return useDraynorBank();
			case DEPOSIT:
				return useDeposit();
			case BUYRUNEHATCHET:
				if (!grandExchangeArea.contains(myLocation()))
					return walkToGE();
				if (!sentOffer
				        && (inventory.contains(995) && inventory.getItem(995).getStackSize() < runeHatchetPrice * 1.1 || !inventory.contains(995)))
					if (!offeredOaks)
						if (!bank.isOpen() && !inventory.contains(1522)) {
							final RSNPC i = npcs.getNearest(BANKERS);
							mouse.move(i.getPoint());
							sleep(100, 200);
							mouse.click(false);
							if (menu.contains("Bank") && menu.clickIndex(menu.getIndex("Bank") + 1))
								return random(1500, 2000);
						} else if (bank.isOpen()) {
							if (bank.getItem(runeHatchetID) != null && !inventory.contains(runeHatchetID)) {
								bank.withdraw(runeHatchetID, 1);
								return 2000;
							}
							if (interfaces.getComponent(762, Bank.INTERFACE_BANK_BUTTON_NOTE).getTextureID() == 1431)
								bank.setWithdrawModeToNote();
							else {
								if (bank.getItem(995) != null) {
									bank.withdraw(995, random(25, 30) * 1000);
									sleep(1000);
								}
								if (bank.getItem(oakLogID) != null) {
									bank.withdraw(oakLogID, 0);
									sleep(1000);
								}
								bank.close();
							}
							return random(1200, 1500);
						}
				if (bankCollectIsOpen())
					bankCollectClose();
				if (!isOpen())
					open();
				if (isOpen()) {
					final int t = getEmptySlot();
					if (t > 0) {
						if (inventory.contains(1522) && getAllEmptySlots() > 0 && !offeredOaks)
							sell("Oak logs", t, inventory.getItem(1522).getStackSize(), (int) (oakPrice * 0.8));
						if (inventory.contains(995) && !sentOffer)
							if (inventory.getItem(995).getStackSize() >= runeHatchetPrice * 1.1)
								buy("Rune hatchet", t, 1, (int) (runeHatchetPrice * 1.1));
						for (int i = 1; i <= getTotalSlots();) {
							if (random(0, 10) == 0)
								mouse.moveSlightly();
							sleep(random(700, 1000));
							if (isAnOfferCompleted()) {
								log("Offer completed.");
								if (isOpen())
									close();
								if (!bankCollectIsOpen())
									bankCollectOpen();
								if (bankCollectIsOpen())
									bankCollectAll();
							}
							i++;
						}
					} else
						for (int i = 1; i <= getTotalSlots();) {
							if (random(0, 10) == 0)
								mouse.moveSlightly();
							sleep(random(700, 1000));
							if (isAnOfferCompleted()) {
								log("Offer completed.");
								if (isOpen())
									close();
								if (!bankCollectIsOpen())
									bankCollectOpen();
								if (bankCollectIsOpen())
									bankCollectAll();
							}
							i++;
						}
				}
				break;
			case BUYHATCHET:
				if (isAnimated())
					return random(500, 1500);
				if (adviserHouse.contains(myLocation())) {
					final RSObject door1 = objects.getTopAt(new RSTile(3228, 3240));
					if (door1 != null)
						if (door1.getID() == 45476) {
							if (door1.isOnScreen())
								door1.interact("Open");
							else
								walking.walkTileMM(door1.getLocation());
							chill();
							return 0;
						}
					walking.walkTileMM(bobsTile);
				}
				if (bobsArea.contains(myLocation()))
					if (interfaces.get(620).isValid()) {
						if (!inventory.contains(bronzeHatchetID)) {
							interfaces.get(620).getComponent(26).getComponent(4).interact("Take 1");
							return 1000;
						}
						if (!inventory.contains(steelHatchetID)) {
							store.getItem(steelHatchetID).interact("Buy 1");
							return 1000;
						}
					} else {
						final RSNPC bob = npcs.getNearest(519);
						if (bob != null)
							if (bob.isOnScreen()) {
								if (bob.interact("Trade Bob"))
									chill();
								return 0;
							} else {
								walking.walkTileMM(bob.getLocation());
								new Camera(bob);
								return 1000;
							}
					}
				if (!bobsArea.contains(myLocation())) {
					final RSObject door = objects.getTopAt(new RSTile(3234, 3203, 0));
					if (door != null)
						if (door.getID() == 45476) {
							if (door.isOnScreen())
								door.interact("Open");
							else
								walking.walkTileMM(door.getLocation());
							chill();
							return 0;
						}
					if (calc.distanceTo(bobsTile) > 100) {
						if (!isAnimated()) {
							magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
						}
						return 2000;
					}
					webWalk(bobsTile);
					return random(500, 1000);
				}
				break;
			case TREE:
				if (store.isOpen()) {
					if ("Bob's Brilliant Axes".contains(interfaces.get(620).getComponent(20).getText())) {
						store.close();
						return 1000;
					}
					if (inventory.contains("Logs")) {
						inventory.getItem("Logs").interact("Sell 50");
						return 1500;
					}
				}
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (trainFM) {
						start = logStart;
						burnLogs = true;
						findNewTile(start, false);
						return 0;
					}
					if (drop) {
						dropLogs = true;
						return 0;
					}
					if (useBank) {
						checkBank = true;
						return 0;
					}
					if (!generalStoreArea.contains(myLocation())) {
						webWalk(generalStoreTile);
						return random(500, 1000);
					}
					final RSNPC shopKeeper = npcs.getNearest(521, 520);
					if (shopKeeper != null)
						if (shopKeeper.isOnScreen()) {
							if (!shopKeeper.interact("Trade Shop"))
								return 0;
							chill();
						} else
							new Camera(shopKeeper);
					return 0;
				}
				final int h = handleCoins();
				if (h != -1)
					return h;
				return chopTree(35, 25, treeTile, "Tree", treeID);
			case OAK:
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (trainFM) {
						start = oakStart;
						burnLogs = true;
						findNewTile(start, false);
						return 0;
					}
					if (drop || powerchop) {
						dropLogs = true;
						return 0;
					}
					checkBank = true;
					return 0;
				}
				if (oakLocation == 1)
					return chopTree(20, 10, oakTreeTile, "Oak", oakID);
				else if (oakLocation == 2)
					return chopTree(30, 20, oakTreeTile2, "Oak", oakID);
				else if (oakLocation == 3)
					return chopTree(15, 6, oakTreeTile3, "Oak", oakID);
				break;
			case WILLOWLUMB:
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (store.isOpen())
						return useStore();
					if (trainFM) {
						start = willowLumbStart;
						burnLogs = true;
						findNewTile(start, false);
						return 0;
					}
					if (drop || powerchop) {
						dropLogs = true;
						return 0;
					}
					if (useBank) {
						checkBank = true;
						return 0;
					}
					if (!generalStoreArea.contains(myLocation())) {
						webWalk(generalStoreTile);
						return random(500, 1000);
					}
					final RSNPC shopKeeper = npcs.getNearest(521, 520);
					if (shopKeeper != null)
						if (shopKeeper.isOnScreen()) {
							if (!shopKeeper.interact("Trade Shop"))
								return 0;
							chill();
						} else
							new Camera(shopKeeper);
					return 0;
				}
				return chopTree(30, 20, willowTreeTile2, "Willow", willowID);
			case WILLOWRIMM:
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (store.isOpen())
						return useStore();
					if (trainFM) {
						start = willowRimmStart;
						burnLogs = true;
						findNewTile(start, false);
						return 0;
					}
					if (drop || powerchop) {
						dropLogs = true;
						return 0;
					}
					if (useBank) {
						useDeposit = true;
						return 0;
					}
					if (!generalStoreArea2.contains(myLocation())) {
						webWalk(generalStoreTile2);
						return random(500, 1000);
					}
					final RSNPC shopKeeper = npcs.getNearest(530, 531);
					if (shopKeeper != null)
						if (shopKeeper.isOnScreen()) {
							if (!shopKeeper.interact("Trade Shop"))
								return 0;
							chill();
						} else
							new Camera(shopKeeper);
					return 0;
				}
				if (calc.distanceTo(draynorBankTile) < 40 && inventory.getCount(dontDropIDs) > 10) {
					checkBank = true;
					return 0;
				}
				if (calc.distanceTo(willowTreeTile) > 20) {
					bankToWillowsPath = walking.newTilePath(willowsToBank).reverse();
					if (bankToWillowsPath.isValid()) {
						bankToWillowsPath.traverse();
						return random(500, 2700);
					}
					webWalk(willowTreeTile);
					return random(500, 1000);
				}
				return chopTree(20, 10, willowTreeTile, "Willow", willowID);
			case WILLOWPORT:
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (trainFM) {
						start = willowPortStart;
						burnLogs = true;
						findNewTile(start, true);
						return 0;
					}
					if (drop || powerchop) {
						dropLogs = true;
						return 0;
					}
					if (useBank) {
						useDeposit = true;
						return 0;
					}
					checkBank = true;
					return 0;
				}
				return chopTree(25, 15, willowTreeTile3, "Willow", willowID);
			case WILLOWDRAY:
				if (dropLogs)
					return dropLogs();
				if (inventory.isFull()) {
					if (trainFM) {
						start = willowDrayStart;
						burnLogs = true;
						findNewTile(start, true);
						return 0;
					}
					if (drop || powerchop) {
						dropLogs = true;
						return 0;
					}
					checkBank = true;
					return 0;
				}
				return chopTree(15, 15, willowTreeTile4, "Willow", willowID);
			case YEW:
				useBank = true; // Always bank Yew logs.
				if (inventory.isFull()) {
					if (yewLocation == 1 || yewLocation == 3)
						useDeposit = true;
					else if (yewLocation == 2)
						checkBank = true;
					return 0;
				}
				if (yewLocation == 1)
					return chopTree(45, 30, yewTreeTile, "Yew", yewID);
				else if (yewLocation == 2)
					return chopTree(40, 30, yewTreeTile2, "Yew", yewID);
				else if (yewLocation == 3)
					return chopTree(25, 15, yewTreeTile3, "Yew", yewID);
				break;
			case LAMPS:
				final RSObject downLadder = objects.getNearest(37683);
				final RSObject door = objects.getTopAt(new RSTile(3228, 3240));
				final RSNPC sirVant = npcs.getNearest(7942);
				final RSItem lamp = inventory.getItem(13439);
				if (sawLamp && lamp == null) {
					final RSObject upLadder = objects.getNearest(37684);
					if (upLadder != null) {
						if (upLadder.isOnScreen())
							upLadder.interact("Climb-up");
						else
							walking.walkTileMM(upLadder.getLocation());
						chill();
						return 1000;
					}
				}
				if (interfaces.get(1139).isValid()) {
					interfaces.get(1139).getComponent(17).interact("Select");
					sleep(50, 200);
					interfaces.get(1139).getComponent(2).interact("Confirm");
					return 1000;
				}
				if (lamp != null) {
					sawLamp = true;
					if (lamp.interact("Rub"))
						return 1500;
				}
				if (interfaces.get(243).isValid())
					if (interfaces.get(243).getComponent(4).containsText("much for the offer")) {
						sawLamp = true;
						return 0;
					}
				if (interfaces.get(228).isValid())
					if (interfaces.get(228).getComponent(2).containsText("I could kill the")) {
						interfaces.get(228).getComponent(2).doClick();
						return 1000;
					}
				if (interfaces.canContinue()) {
					interfaces.clickContinue();
					return random(800, 1900);
				}
				if (sirVant != null && calc.distanceTo(sirVant) < 10) {
					if (sirVant.isOnScreen()) {
						if (!sirVant.interact("Talk-to"))
							return 0;
						else
							chill();
					} else {
						walking.walkTileMM(sirVant.getLocation());
						chill();
					}
					return 500;
				}
				if (!adviserHouse.contains(myLocation())) {
					if (door != null && door.getID() == 45476)
						if (door.isOnScreen()) {
							door.interact("Open");
							chill();
							return 1000;
						} else {
							walking.walkTileMM(door.getLocation());
							return random(500, 1700);
						}
				}
				if (downLadder != null) {
					if (downLadder.isOnScreen()) {
						if (!downLadder.interact("Climb-down"))
							return 0;
					} else {
						walking.walkTileMM(downLadder.getLocation());
						new Camera(downLadder);
					}
					chill();
					return 1000;
				}
				webWalk(new RSTile(3226, 3240, 0));
				sleep(random(500, 1000));
				break;
		} // end of switch
		return random(300, 500);
	}
	private int[] reverse(int[] b) {
		for (int left = 0, right = b.length - 1; left < right; left++, right--) {
			int temp = b[left];
			b[left] = b[right];
			b[right] = temp;
		}
		return b;
	}
	private int dropLogs() {
		final RSItem[] logs = inventory.getItems(logIDs);
		if (logs.length == 0)
			dropLogs = false;
		int[] chosenPath = null;
		switch (random(0, 6)) {
			case 0:
				if (random(0, 2) == 0)
					for (int i = random(0, logs.length - 1); i < logs.length && i >= 0; i++) {
						logs[i].interact("Drop");
						final int count = inventory.getCount(logIDs);
						if (count > 5 && random(0, 10) == 0)
							i++;
					}
				else
					for (int i = random(0, logs.length - 1); i >= 0; i--) {
						logs[i].interact("Drop");
						final int count = inventory.getCount(logIDs);
						if (count > 5 && random(0, 10) == 0)
							i--;
					}
				return 0;
			case 1:
				if (random(0, 2) == 0)
					chosenPath = dropPath1;
				else
					chosenPath = reverse(dropPath1);
				break;
			case 2:
				if (random(0, 2) == 0)
					chosenPath = dropPath2;
				else
					chosenPath = reverse(dropPath2);
				break;
			case 3:
				if (random(0, 2) == 0)
					chosenPath = dropPath3;
				else
					chosenPath = reverse(dropPath3);
				break;
			case 4:
				if (random(0, 2) == 0)
					chosenPath = dropPath4;
				else
					chosenPath = reverse(dropPath4);
				break;
			case 5:
				if (random(0, 2) == 0)
					chosenPath = dropPath5;
				else
					chosenPath = reverse(dropPath5);
				break;
		}
		for (int i : chosenPath) {
			RSItem log = inventory.getItemAt(i - 1);
			for (int id : logIDs) {
				if (log.getID() == id) {
					while (log != null && !log.interact("Drop"))
						// TODO Does it work well?
						log = inventory.getItemAt(i - 1);
				}
			}
		}
		if (inventory.getCount(logIDs) < 3)
			dropLogs = false;
		return random(100, 300);
	}
	private int walkToGE() {
		pathToGE = walking.newTilePath(toGe);
		pathToGE2 = walking.newTilePath(toGe2);
		if (calc.distanceTo(pathToGE.getEnd()) > 25 && pathToGE.isValid()) {
			pathToGE.traverse();
			return random(500, 2700);
		}
		if (calc.distanceTo(pathToGE2.getEnd()) > 25 && pathToGE2.isValid()) {
			pathToGE2.traverse();
			return random(500, 2700);
		}
		webWalk(GETile);
		return random(300, 1500);
	}
	private int useStore() {
		for (final int i : logIDs)
			if (inventory.contains(i) && inventory.getItem(i).interact("Sell 50"))
				return random(1000, 1500);
		return 0;
	}
	private int useDeposit() {
		if (bank.isDepositOpen()) {
			if (depositAllExcept(dontDepositIDs))
				useDeposit = false;
			return 0;
		}
		final RSObject depositBox = objects.getNearest(Bank.DEPOSIT_BOXES);
		if (depositBox != null && depositBox.isOnScreen()) {
			if (!depositBox.interact("Deposit " + depositBox.getName())) {
				new Camera(depositBox);
				return 0;
			}
			chill();
			return random(700, 1500);
		}
		webWalk(new RSTile(3047, 3235));
		return random(500, 1000);
	}
	private boolean depositAllExcept(int[] ids) {
		int money = inventory.contains(995) ? inventory.getItem(995).getStackSize() : 0;
		RSItem deposit = null;
		outer: for (RSItem r : inventory.getItems()) {
			for (int i : ids) {
				if (r.getID() == i) {
					continue outer;
				}
			}
			if (!r.hasAction("Drop")) {
				continue;
			} else if (deposit == null || r.getID() != deposit.getID()) {
				deposit = r;
				if (bank.deposit(deposit.getID(), 0)) {
					if (deposit.getID() == 995)
						bankCash += money;
					sleep(random(300, 500));
				}
				continue outer;
			}
		}
		if (deposit == null)
			return true;
		return false;
	}
	private int checkStuff() {
		if (end) {
			env.disableRandom("Improved Login");
			if (!game.isLoggedIn())
				stopScript(false);
			game.logout(false);
			return 1000;
		}
		if (!loading) {
			loading = true;
			if (currVer == -1) {
				new InformationLoader(InformationLoader.VERSION);
				return 0;
			}
			if (!checkedGE) {
				new InformationLoader(InformationLoader.CHECKGE);
				return 0;
			}
		}
		if (isLoggedIn()) {
			if (initialXP == -1) {
				initialXP = skills.getCurrentExp(Skills.WOODCUTTING);
				initialXP2 = skills.getCurrentExp(Skills.FIREMAKING);
				initialLevel = wcLvl();
				initialLevel2 = fmLvl();
				startTime = System.currentTimeMillis();
				screenshotTimer = new Timer(7200000);
				antibanTimer = startTime + random(5000, 10000);
				status = "";
			}
		} else {
			return 1000;
		}
		if (wait) {
			return 100;
		}
		if (screenshotTimer != null && !screenshotTimer.isRunning()) {
			log("Taking screenshot.");
			env.saveScreenshot(true);
			screenshotTimer.reset();
		}
		totalCash = inventory.getItem(995) == null ? bankCash + oakCash : inventory.getItem(995).getStackSize()
		        + bankCash + oakCash;
		final RSItem[] invent = inventory.getItems();
		outer: for (final RSItem r : invent) {
			if (r.hasAction("Eat")) {
				r.interact("Eat");
				sleep(700);
			}
			if (!r.hasAction("Drop") || r.getName().toLowerCase().contains("hatchet"))
				continue;
			for (final int h : dontDropIDs) {
				if (r.getID() == h)
					continue outer;
			}
			r.interact("Drop");
		}
		if (interfaces.get(149).isValid()) {
			sleep(random(300, 500));
			if (!interfaces.get(149).getComponent(230).interact("Select"))
				return 500;
			log("Closed level 10 Woodcutting solicitation message.");
		}
		if (interfaces.get(1066).isValid()) {
			sleep(random(300, 500));
			interfaces.get(1066).getComponent(34).doHover();
			if (!interfaces.get(1066).getComponent(34).interact("Close"))
				return 500;
			log("Closed Entrana monk solicitation message.");
		}
		if (!checkBank && !store.isOpen() && !bank.isOpen() && !bank.isDepositOpen()) {
			bestHatchetAvailable = bestHatchetAvailable();
			for (int i : hatchetIDs)
				if (!useAvailableHatchets && i != bestHatchetAvailable && inventory.contains(i))
					inventory.dropItem(i);
		}
		if (!walking.isRunEnabled() && run < walking.getEnergy()) {
			interfaces.getComponent(walking.INTERFACE_RUN_ORB, 0).doHover();
			if (!interfaces.getComponent(walking.INTERFACE_RUN_ORB, 0).interact("Turn run mode on")) {
				mouse.moveRandomly(20);
				return 0;
			}
			run = random(5, 30);
			sleep(500, 800);
		}
		if (myLocation().getZ() != 0) {
			final int myFloor = myLocation().getZ();
			log("How did you get up there?");
			int dist = 20;
			RSObject stairs = null;
			for (final RSObject o : objects.getAll(2348, 9471, 9559, 36769, 36770, 36774, 36775, 36777, 36778, 39329,
			    40059, 45482, 45484, 55450))
				if (o.getLocation().getZ() == myFloor) {
					if (calc.distanceTo(o) < dist) {
						dist = calc.distanceTo(o);
						stairs = o;
					}
				}
			if (stairs == null) {
				log("There is no way to get down.");
				end = true;
				return 0;
			}
			if (calc.tileOnScreen(stairs.getLocation())) {
				mouse.move(stairs.getPoint());
				if (stairs.hasAction("Climb-up") && myLocation().getZ() < 0) {
					stairs.interact("Climb-up");
				} else if (stairs.hasAction("Climb-down") && myLocation().getZ() > 0) {
					stairs.interact("Climb-down");
				} else if (stairs.doClick()) {
					log("Climbing...");
					chill();
					sleep(2000);
				} else
					end = true;
			} else {
				walking.walkTileMM(stairs.getLocation());
				new Camera(stairs);
				chill();
			}
			return random(1000, 1500);
		}
		return -1;
	}
	private boolean isLoggedIn() {
	    return interfaces.get(137).isValid() && (game.getClientState() == 11 || game.isLoading());
    }
	private String manageDots() {
		if (!dotTimer.isRunning()) {
			dotTimer.reset();
			if (dots.length() >= 3)
				dots = "";
			else
				dots = dots + ".";
		}
		return dots;
	}
	private void webWalk(final RSTile dest) {
		RSWeb walkWeb = null;
		try {
			walkWeb = web.getWeb(dest);
		} catch (final Exception e) {
			log("Exception: " + e);
		}
		if (bobsArea.contains(myLocation())) {
			final RSObject door = objects.getTopAt(new RSTile(3234, 3203, 0));
			if (door != null) {
				if (door.getID() == 45476) {
					if (door.isOnScreen())
						door.interact("Open");
					else
						walking.walkTileMM(door.getLocation());
					chill();
				}
				return;
			}
		}
		if (adviserHouse.contains(myLocation())) {
			final RSObject door1 = objects.getTopAt(new RSTile(3228, 3240));
			if (door1 != null) {
				if (door1.getID() == 45476) {
					if (door1.isOnScreen())
						door1.interact("Open");
					else
						walking.walkTileMM(door1.getLocation());
					chill();
				}
				return;
			}
		}
		if (walkWeb != null) {
			if (!getMyPlayer().isMoving() || calc.distanceTo(walking.getDestination()) < 5) {
				walkWeb.step();
				if (calc.distanceTo(dest) <= 5)
					walking.walkTileMM(walkWeb.getEnd().randomize(2, 2));
				antiBan();
			}
		} else {
			if (calc.distanceTo(walking.getDestination()) <= 5) {
				walking.walkTileMM(dest.randomize(2, 2));
			}
		}
	}
	private RSTile myLocation() {
		return getMyPlayer().getLocation();
	}
	private int wcLvl() {
		return skills.getRealLevel(Skills.WOODCUTTING);
	}
	private int fmLvl() {
		return skills.getRealLevel(Skills.FIREMAKING);
	}
	private void chill() {
		sleep(1000);
		while (getMyPlayer().isMoving() && !isPaused()) {
			if (store.isOpen())
				break;
			sleep(100);
		}
	}
	private int useDraynorBank() {
		final RSNPC banker = npcs.getNearest(Bank.BANKERS);
		if (!bank.isOpen() && banker != null && calc.distanceTo(banker) < 20) {
			if (banker.isOnScreen()) {
				mouse.move(banker.getPoint());
				mouse.click(false);
				sleep(random(10, 40));
				if (!menu.contains("Bank"))
					mouse.moveRandomly(200);
				if (menu.contains("Bank") && !menu.clickIndex(menu.getIndex("Bank") + 1))
					return 0;
			} else {
				new Camera(banker);
				walking.walkTileMM(banker.getLocation());
			}
			// banker.interact("Talk-to"); // TODO ... "Bank" clicks "Talk-to"
			chill();
			return 0;
		} else if (bank.isOpen()) {
			if (inventory.getCount() > 0 && !bank.depositAll())
				return 0;
			sleep(random(1500, 2000));
			final RSItem[] bankItems = bank.getItems();
			for (final RSItem r : bankItems) {
				final String name = r.getName();
				if (name.contains("Bronze hatchet"))
					hasBronzeHatchet = true;
				if (name.contains("Iron hatchet"))
					hasIronHatchet = true;
				if (name.contains("Steel hatchet"))
					hasSteelHatchet = true;
				if (name.contains("Black hatchet"))
					hasBlackHatchet = true;
				if (name.contains("Mithril hatchet"))
					hasMithrilHatchet = true;
				if (name.contains("Adamant hatchet"))
					hasAdamantHatchet = true;
				if (name.contains("Rune hatchet"))
					hasRuneHatchet = true;
			}
			bestHatchetAvailable = bestHatchetAvailable();
			final RSItem coins = bank.getItem("Coins");
			if (coins != null)
				bankCash = coins.getStackSize();
			else
				bankCash = 0;
			final RSItem oaks = bank.getItem("Oak logs");
			if (oaks != null)
				oakCash = oaks.getStackSize() * oakPrice;
			else
				oakCash = 0;
			if (bestHatchetAvailable != -1 && bank.getItem(bestHatchetAvailable) != null
			        && !inventory.contains(bestHatchetAvailable)) {
				bank.withdraw(bestHatchetAvailable, 1);
				bankFails = 0;
				sleep(random(1500, 2000));
			} else if (inventory.contains(bestHatchetAvailable))
				sleep(random(500, 1000));
			else if (bankFails > 3) {
				log("No hatchet available for your current level");
				end = true;
				return 0;
			} else {
				log("Bank fail count: " + ++bankFails);
				return random(1000, 2000);
			}
			if (trainFM && !inventory.contains(tinderboxID)) {
				if (bank.getItem(tinderboxID) != null)
					bank.withdraw(tinderboxID, 1);
				sleep(random(1500, 2000));
			}
			if (inventory.contains(bestHatchetAvailable) && bestHatchetAvailable != -1
			        && (!trainFM || trainFM && inventory.contains(tinderboxID))) {
				checkBank = false;
				checkedBank = true;
				bank.close();
			}
		}
		if (!draynorBankArea.contains(myLocation())) {
			willowsToBankPath = walking.newTilePath(willowsToBank);
			if (willowsToBankPath.isValid()) {
				willowsToBankPath.traverse();
				return random(500, 2700);
			}
			webWalk(draynorBankTile);
			return random(500, 1000);
		}
		return 0;
	}
	/**
	 * @author hlunnb
	 * @param farDist The distance away from the centre where web walking is used to get back.
	 * @param closeDist The distance away from the centre where it will click the centre and wait until it walks back,
	 * trees should be inside this area.
	 * @param t The centre tile.
	 * @param treeName The name of the tree. "Willow"
	 * @param treeID An array tree IDs
	 * @return A number representing the amount of time to sleep based on the action completed. Should return this value
	 * in loop.
	 */
	private int chopTree(final int farDist, final int closeDist, final RSTile t, final String treeName,
	        final int[] treeID) {
		if (powerchop && !trainFM) {
			final RSItem item1 = inventory.getItem(moveIDs);
			final int idx = item1.getComponent().getComponentIndex();
			if (idx == 0 || idx == 1 || idx == 4 || idx == 5) {
				int x = 24;
				RSItem item2 = inventory.getItemAt(x);
				checkIDs: for (final int i : moveIDs)
					if (item2.getID() == i) {
						x += 1;
						item2 = inventory.getItemAt(x);
						break checkIDs;
					}
				item1.getComponent().doHover();
				RSComponent component = item2.getComponent();
				mouse.drag(random(component.getAbsoluteX(), component.getAbsoluteX() + component.getWidth()),
				    random(component.getAbsoluteY(), component.getAbsoluteY() + component.getHeight()));
				logCount = inventory.getCount(regularLogID, oakLogID, willowLogID);
			}
		}
		if (calc.distanceTo(t) > farDist) {
			webWalk(t);
			return random(500, 1000);
		}
		if (calc.distanceTo(t) > closeDist) {
			walking.walkTileMM(t);
			chill();
		}
		if (calc.distanceTo(t) <= closeDist) {
			RSObject tree = getNearestTree(treeID, t, closeDist);
			if (tree != null) {
				if (clickTree && calc.tileOnScreen(tree.getLocation())) {
					new Camera(Camera.PITCH);
					if (!tiles.interact(tree.getLocation(), "Chop down " + treeName))
						return 0;
					clickTree = false;
					dropTimer.reset();
					return 0;
				}
				if (!dropTimer.isRunning()
				        && ((treeID == this.treeID || treeID == willowID) && distanceTo(tree) > 1 || (treeID == oakID || treeID == yewID)
				                && distanceTo(tree) > 2))
					if (tree.isOnScreen()) {
						if (tree.getModel().interact("Chop down " + treeName)) {
							treeFails = 0;
							chill();
							logCount = inventory.getCount(regularLogID, oakLogID, willowLogID);
							clickTree = false;
						} else {
							treeFails++;
							if (treeFails > 5)
								new Camera(tree);
						}
						return 0;
					} else {
						if (camera.getAngleTo(camera.getObjectAngle(tree)) > 30)
							new Camera(tree);
						if (calc.distanceTo(walking.getDestination()) < 7 || isIdle()) {
							walking.walkTileMM(tree.getLocation());
							return random(800, 1500);
						}
						return 0;
					}
				if (!dropTimer.isRunning() && !isAnimated()) {
					tree = getNearestTree(treeID, t, closeDist);
					if (tree == null)
						return 0;
					if (tree.isOnScreen()) {
						if (tree.getModel().interact("Chop down " + treeName)) {
							treeFails = 0;
							chill();
							logCount = inventory.getCount(regularLogID, oakLogID, willowLogID);
							clickTree = false;
						} else {
							treeFails++;
							if (treeFails > 3)
								new Camera(tree);
						}
						return 0;
					} else {
						if (camera.getAngleTo(camera.getObjectAngle(tree)) > 30)
							new Camera(tree);
						if (calc.distanceTo(walking.getDestination()) < 7 || isIdle()) {
							walking.walkTileMM(tree.getLocation());
							return random(800, 1500);
						}
						return 0;
					}
				}
			} else if (calc.distanceTo(t) > 5) {
				webWalk(t.randomize(3, 3));
				return random(800, 1500);
			}
		}
		if (!powerchop && isAnimated())
			antiBan();
		if (powerchop && !trainFM) {
			if (invLog == null || !invLog.hasAction("Drop"))
				invLog = inventory.getItem(regularLogID, oakLogID, willowLogID);
			final int logs = inventory.getCount(regularLogID, oakLogID, willowLogID);
			if (!dropTimer.isRunning() && logCount < logs
			        && inventory.getCount(regularLogID, oakLogID, willowLogID) > 3 && inventory.getCount() < 27) {
				new Camera(Camera.PITCH);
				if (!invLog.getComponent().contains(mouse.getLocation().x, mouse.getLocation().y)) {
					invLog.getComponent().doHover();
					return 0;
				}
				mouse.click(false);
				sleep(random(10, 40));
				mouse.hop(mouse.getLocation().x, mouse.getLocation().y + 40);
				sleep(random(10, 40));
				mouse.click(true);
				logCount = logs - 1;
				clickTree = true;
				final RSItem invLogOld = invLog;
				boolean continued = false;
				for (final RSItem i : inventory.getItems())
					if (i.getName().toLowerCase(Locale.ENGLISH).contains("logs")
					        && !i.getPoint().equals(invLog.getPoint()) && i.hasAction("Drop")) {
						if (random(0, 10) == 0 && !continued) {
							continued = true;
							continue;
						}
						invLog = i;
						break;
					}
				if (random(0, 5) == 0) {
					if (!invLog.getComponent().doHover())
						return 0;
					mouse.click(false);
					sleep(random(10, 40));
					mouse.hop(mouse.getLocation().x, mouse.getLocation().y + 50);
					sleep(random(10, 40));
					mouse.click(true);
					logCount--;
					for (final RSItem i : inventory.getItems())
						if (i.getName().toLowerCase(Locale.ENGLISH).contains("logs")
						        && !i.getPoint().equals(invLog.getPoint())
						        && !i.getPoint().equals(invLogOld.getPoint()) && i.hasAction("Drop")) {
							invLog = i;
							break;
						}
				}
			}
			if (invLog != null && !clickTree)
				if (!invLog.getComponent().contains(mouse.getLocation().x, mouse.getLocation().y)) {
					new Camera(Camera.PITCH);
					invLog.getComponent().doHover();
				} else if (rpTimer == null || !rpTimer.isRunning()) {
					final Rectangle r = invLog.getComponent().getArea();
					final Point rp = new Point((int) (r.getX() + r.getWidth() * random(0, 1000) / 1000),
					    (int) (r.getY() + r.getHeight() * random(0, 1000) / 1000));
					mouse.move(rp);
					rpTimer = new Timer(random(2000, 15000));
				} else {
					antiBan();
					logCount = inventory.getCount(regularLogID, oakLogID, willowLogID);
				}
		}
		return 0;
	}
	private int distanceTo(final RSObject t) {
		final int xDist = Math.abs(myLocation().getX() - t.getLocation().getX());
		final int yDist = Math.abs(myLocation().getY() - t.getLocation().getY());
		return xDist > yDist ? xDist : yDist;
	}
	/**
	 * Returns the <tt>RSObject</tt> that is nearest and a within certain distance away a centre tile.
	 * @param treeID The array of tree ids.
	 * @param t The centre tile.
	 * @param closeDist The maximum distance from the centre tile.
	 * @return An <tt>RSObject</tt> representing the nearest tree within the approved distance or null if a tree cannot
	 * be found.
	 */
	private RSObject getNearestTree(final int[] treeID, final RSTile t, final int closeDist) {
		RSObject tree = null;
		RSObject tree2;
		int dist = 100000000;
		for (final int i : treeID) {
			tree2 = objects.getNearest(i);
			if (tree2 == null || calc.distanceBetween(t, tree2.getLocation()) >= closeDist)
				continue;
			if (calc.distanceTo(tree2.getLocation()) < dist) {
				dist = calc.distanceTo(tree2.getLocation());
				tree = objects.getNearest(i);
			}
		}
		return tree;
	}
	private void burnLog() {
		if (isSelected())
			if (timer2 < System.currentTimeMillis() && isIdle()) {
				if (clickTin())
					timer2 = System.currentTimeMillis() + random(1600, 1700);
			} else
				hoverTin();
	}
	private void hoverTin() {
		final RSItem tin = inventory.getItem(tinderboxID);
		tin.getComponent().doHover();
	}
	private boolean clickTin() {
		final RSItem tin = inventory.getItem(tinderboxID);
		if (tin != null && tin.interact("Use"))
			return true;
		return false;
	}
	private boolean clickLog() {
		final RSItem[] logs = inventory.getItems(false);
		RSItem log = null;
		for (final RSItem l : logs) {
			if (l == null)
				continue;
			if (dontClick != null)
				if (l.getComponent().equals(dontClick.getComponent()))
					continue;
			if (!canBurn(l)) {
				inventory.dropItem(l);
				sleep(random(500, 750));
				continue;
			}
			if (l.getID() == 1519 || l.getID() == 1511 || l.getID() == 1521 || l.getID() == 1515) {
				log = l;
				break;
			}
		}
		if (log != null)
			if (log.interact("Use")) {
				if (inventory.getCount(1519, 1511, 1521, 1515) > 1)
					dontClick = log;
				return true;
			}
		return false;
	}
	private boolean canBurn(final RSItem l) {
		if (l.getID() == 1515)
			return fmLvl() >= 60 ? true : false;
		if (l.getID() == 1519)
			return fmLvl() >= 30 ? true : false;
		if (l.getID() == 1521)
			return fmLvl() >= 15 ? true : false;
		if (l.getID() == 1511)
			return fmLvl() >= 1 ? true : false;
		return true; // If the item doesn't have a log ID.
	}
	/**
	 * @return True, if the player isn't moving and has no animation.
	 */
	private boolean isIdle() {
		return !isAnimated() && !getMyPlayer().isMoving();
	}
	private boolean isSelected() {
		final RSItem inv = inventory.getSelectedItem();
		if (inv != null) {
			final int id = inv.getID();
			return id == 1519 || id == 1511 || id == 1521 || id == 1515;
		}
		return false;
	}
	private boolean isTileFree(final RSTile t) {
		final RSObject[] objs = objects.getAllAt(t);
		for (final RSObject o : objs)
			if (o.getID() == 2732)
				return false;
		return true;
	}
	/**
	 * @param r An array of starting tiles.
	 * @param p True to use priority, false to use closest method.
	 * @return True if a new destination was choosen, false otherwise.
	 */
	private boolean findNewTile(final RSTile[] r, final boolean p) {
		if (!p) {
			int dist = 1000000000;
			for (final RSTile t : start)
				if (isTileFree(t))
					if (calc.distanceTo(t) < dist) {
						dest = t;
						dist = calc.distanceTo(t);
					}
		} else
			for (final RSTile t : start)
				if (isTileFree(t)) {
					RSTile alt = start[random(0, start.length)];
					if (t == start[0] && random(0, 5) == 0 && isTileFree(alt)) {
						dest = alt;
						break;
					}
					dest = t;
				}
		if (dest == null)
			return false;
		return true;
	}
	private int walkDest() {
		if (myLocation().equals(dest) && !getMyPlayer().isMoving()) {
			dest = null;
			return 0;
		}
		new Camera(Camera.PITCH);
		final Point p = calc.tileToScreen(dest);
		if (calc.pointOnScreen(p)) {
			mouse.move(p);
			if (menu.contains("Walk here")) {
				tiles.interact(dest, "Walk here");
				chill();
				return 0;
			} else {
				walking.walkTileMM(dest);
				return random(700, 1500);
			}
		}
		webWalk(dest);
		return random(500, 1000);
	}
	private int take(final RSGroundItem g) {
		if (calc.tileOnScreen(g.getLocation())) {
			if (g.interact("Take " + g.getItem().getName()))
				chill();
		} else if (!getMyPlayer().isMoving()) {
			walking.walkTileMM(g.getLocation());
			antiBan();
			return random(500, 2000);
		}
		return 0;
	}
	private int handleCoins() {
		if (!isAnimated() && (bestHatchetAvailable == -1 || bestHatchetAvailable == bronzeHatchetID)) {
			final RSGroundItem coins = groundItems.getNearest(995);
			if (coins != null
			        && calc.distanceTo(coins.getLocation()) < 20
			        && (inventory.getItem(995) == null || inventory.getItem(995) != null
			                && inventory.getItem(995).getStackSize() < 200))
				return take(coins);
		}
		return -1;
	}
	/**
	 * @return The highest tier hatchet in the players inventory or bank at the current level.
	 */
	private int bestHatchetAvailable() {
		final int wcLvl = wcLvl();
		if (hasRuneHatchet || inventory.contains(runeHatchetID))
			if (wcLvl >= 41)
				return runeHatchetID;
		if (hasAdamantHatchet || inventory.contains(adamantHatchetID))
			if (wcLvl >= 31)
				return adamantHatchetID;
		if (hasMithrilHatchet || inventory.contains(mithrilHatchetID))
			if (wcLvl >= 21)
				return mithrilHatchetID;
		if (hasBlackHatchet || inventory.contains(blackHatchetID))
			if (wcLvl >= 6)
				return blackHatchetID;
		if (hasSteelHatchet || inventory.contains(steelHatchetID))
			if (wcLvl >= 6)
				return steelHatchetID;
		if (hasIronHatchet || inventory.contains(ironHatchetID))
			return ironHatchetID;
		if (hasBronzeHatchet || inventory.contains(bronzeHatchetID))
			return bronzeHatchetID;
		return -1;
	}
	private boolean isAnimated() {
		return getMyPlayer().getAnimation() != -1;
	}
	private double getCurrentVersion() {
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(new URL(
			    "http://pastebin.com/raw.php?i=SePHdUFV").openStream()));
			final double d = Double.parseDouble(r.readLine());
			r.close();
			return d;
		} catch (final Exception e) {
			log.warning("Error checking for latest version.");
		}
		return scriptVersion;
	}
	private void sendToURL(final String url) {
		try {
			java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
		} catch (final Exception e) {}
	}

	class InformationLoader extends Thread {
		public static final int VERSION = 0;
		public static final int CHECKGE = 1;
		int i;
		InformationLoader(int i) {
			this.i = i;
			this.start();
		}
		public void run() {
			switch (i) {
				case 0:
					currVer = 0;
					currVer = getCurrentVersion();
					DecimalFormat format = new DecimalFormat("0.00");
					if (scriptVersion >= currVer)
						log(Color.green, "Your version: " + format.format(scriptVersion) + ", Latest version: "
						        + format.format(currVer) + ". Your script is up to date.");
					else
						log(Color.red,
						    "Your version: " + format.format(scriptVersion) + ", Latest version: "
						            + format.format(currVer) + ". You should get the latest version.");
					break;
				case 1:
					checkedGE = true;
					try {
						log("Loading prices from Grand Exchange.");
						oakPrice = grandExchange.lookup("Oak logs").getGuidePrice();
						runeHatchetPrice = grandExchange.lookup("Rune hatchet").getGuidePrice();
					} catch (final Exception e) {
						checkedGE = true;
						log.severe("Could not get Grand Exchange prices.");
						final int sleep = random(60, 300);
						log("Will try again in: " + sleep + " seconds.");
						final long stopTime = System.currentTimeMillis() + sleep * 1000;
						while (System.currentTimeMillis() < stopTime && scriptRunning)
							try {
								sleep(100);
							} catch (final InterruptedException e1) {
								e1.printStackTrace();
							}
						checkedGE = false;
						return;
					}
					if (oakPrice != -1 && runeHatchetPrice != -1)
						log("Rune hatchet price: " + runeHatchetPrice + " gp, " + "Oak price: " + oakPrice + " gp.");
					else {
						oakPrice = 30; // Set lower.
						runeHatchetPrice = 10000; // Set higher.
					}
					break;
			}
			loading = false;
		}
	}

	class Camera extends Thread {
		public static final int ADVANCED = 0;
		public static final int PITCH = 1;
		private RSTile r = null;
		private int antiban = -1;
		private int min = 500;
		private int max = 1500;
		private Camera(final RSObject r) {
			this.r = r.getLocation();
			this.start();
		}
		private Camera(final RSNPC r) {
			this.r = r.getLocation();
			this.start();
		}
		private Camera(final RSTile r) {
			this.r = r;
			this.start();
		}
		private Camera(final int antiban) {
			this.antiban = antiban;
			this.start();
		}
		private Camera(final int min, final int max) {
			antiban = ADVANCED;
			this.min = min;
			this.max = max;
			this.start();
		}
		@Override
		public void run() {
			try {
				if (r != null)
					if (camera.getPitch() < 25 || random(0, 2) == 0 && camera.getPitch() < 60) {
						keyboard.pressKey((char) KeyEvent.VK_UP);
						final Timer t = new Timer(random(500, 1000));
						if (camera.getAngleTo(camera.getTileAngle(r)) > random(45, 70))
							camera.turnTo(r, 30);
						sleep(t.getRemaining());
						keyboard.releaseKey((char) KeyEvent.VK_UP);
					} else
						camera.turnTo(r, 30);
				if (antiban != -1)
					switch (antiban) {
						case ADVANCED:
							advancedCameraMovement(min, max);
							break;
						case PITCH:
							if (camera.getPitch() < random(30, 55)) {
								camera.setPitch(random(70, 100));
							}
							break;
					}
				antiBan = "";
			} catch (final InterruptedException e) {}
		}
	}

	class PaintUpdater extends Thread {
		PaintUpdater() {
			this.start();
		}
		public void run() {
			while (scriptRunning) {
				if (isPaused())
					env.setUserInput(Environment.INPUT_MOUSE | Environment.INPUT_KEYBOARD);
				else
					env.setUserInput(Environment.INPUT_KEYBOARD);
				if (!globeSelected && !locked) {
					final int wc = wcLvl();
					if (trainFM) {
						final int fm = fmLvl();
						if (fm >= 30) {
							willowSelected = true;
							treeSelected = false;
							oakSelected = false;
						} else if (fm >= 15) {
							oakSelected = true;
							treeSelected = false;
							willowSelected = false;
						} else {
							treeSelected = true;
							oakSelected = false;
							willowSelected = false;
						}
					} else if (wc >= 60 && yewAfter60) {
						yewSelected = true;
						treeSelected = false;
						oakSelected = false;
						willowSelected = false;
					} else if (wc >= 30) {
						willowSelected = true;
						treeSelected = false;
						oakSelected = false;
						yewSelected = false;
					} else if (wc >= 15) {
						oakSelected = true;
						treeSelected = false;
						willowSelected = false;
						yewSelected = false;
					} else {
						treeSelected = true;
						oakSelected = false;
						willowSelected = false;
						yewSelected = false;
					}
				}
				try {
					sleep(100);
				} catch (final InterruptedException e) {}
			}
		}
	}
	public void mouseEntered(final MouseEvent e) {}
	public void mouseExited(final MouseEvent e) {}
	public void mouseReleased(final MouseEvent e) {
		if (moving) {
			mp.x = e.getX() - mp.xLoc;
			mp.y = e.getY() - mp.yLoc;
		}
		moving = false;
	}
	public void mouseDragged(final MouseEvent e) {
		if (moving) {
			mp.x = e.getX() - mp.xLoc;
			mp.y = e.getY() - mp.yLoc;
		}
	}
	public void mouseMoved(final MouseEvent e) {
		final Rectangle infoArea = new Rectangle(490, 59, 25, 25);
		final Rectangle clickArea = new Rectangle(240, 150, 50, 50);
		final Rectangle settingsArea = new Rectangle(490, 86, 25, 25);
		final Rectangle fmArea = new Rectangle(490, 32, 25, 25);
		final Rectangle worldArea = new Rectangle(490, 5, 25, 25);
		final Rectangle optionsOpenArea = new Rectangle(285, 0, 205, 110);
		final Point a = e.getPoint();
		if (infoArea.contains(a))
			infoSelected = true;
		else
			infoSelected = false;
		if (!playClicked && clickArea.contains(a))
			playSelected = true;
		else
			playSelected = false;
		if (settingsArea.contains(a))
			settingsSelected = true;
		else
			settingsSelected = false;
		if (fmArea.contains(a))
			trainFMHovered = true;
		else
			trainFMHovered = false;
		if (worldArea.contains(a))
			globeSelected = true;
		else if (!optionsOpenArea.contains(a))
			globeSelected = false;
	}
	public void mousePressed(final MouseEvent e) {
		if (!isLoggedIn())
			return;
		final Rectangle showArea = new Rectangle(mp.x + mp.w - 34, mp.y + mp.h - 19, 34, 19);
		final Rectangle fmArea = new Rectangle(490, 32, 25, 25);
		final Rectangle lockArea = new Rectangle(462, 5, 24, 24);
		final Rectangle infoArea = new Rectangle(490, 59, 25, 25);
		final Rectangle settingsArea = new Rectangle(490, 86, 25, 25);
		final Rectangle startArea = new Rectangle(240, 150, 50, 50);
		final Rectangle logArea = new Rectangle(336, 3, 31, 25);
		final Rectangle oakArea = new Rectangle(367, 3, 31, 25);
		final Rectangle willowArea = new Rectangle(398, 3, 31, 25);
		final Rectangle yewArea = new Rectangle(429, 3, 31, 25);
		final Rectangle idx1 = new Rectangle(341, 28, 124, 20);
		final Rectangle idx2 = new Rectangle(341, 48, 124, 20);
		final Rectangle idx3 = new Rectangle(341, 68, 124, 20);
		final Rectangle idx4 = new Rectangle(341, 105 - 17, 124, 20);
		final Rectangle mode1 = new Rectangle(285, 28, 51, 20);
		final Rectangle mode2 = new Rectangle(285, 48, 51, 20);
		final Rectangle mode3 = new Rectangle(285, 68, 51, 20);
		final Rectangle mode4 = new Rectangle(285, 88, 51, 20);
		final Rectangle cutYewArea = new Rectangle(469, 345, 25, 25);
		final Rectangle indHatchet = new Rectangle(469, 395, 25, 25);
		final Rectangle aHatchet = new Rectangle(469, 425, 25, 25);
		final Rectangle checkBankArea = new Rectangle(200, 345, 25, 25);
		final Point a = e.getPoint();
		if (showPaint && mp.moveBox.contains(a) && !showArea.contains(a)) {
			moving = true;
			mp.xLoc = e.getX() - mp.x;
			mp.yLoc = e.getY() - mp.y;
		}
		if (!playClicked) {
			if (indHatchet.contains(a))
				useAvailableHatchetsSelected = false;
			if (aHatchet.contains(a))
				useAvailableHatchetsSelected = true;
			if (cutYewArea.contains(a))
				yewsAfter60Selected = !yewsAfter60Selected;
			if (checkBankArea.contains(a))
				checkBankSelected = !checkBankSelected;
			if (startArea.contains(a)) {
				if (wait) {
					startTime = System.currentTimeMillis();
					screenshotTimer = new Timer(7200000);
					antibanTimer = startTime + random(5000, 10000);
					initialXP = skills.getCurrentExp(Skills.WOODCUTTING);
					initialXP2 = skills.getCurrentExp(Skills.FIREMAKING);
				}
				playClicked = true;
				showSettings = false;
				wait = false;
				showPaint = true;
				useAvailableHatchets = useAvailableHatchetsSelected;
				yewAfter60 = yewsAfter60Selected;
				checkBank = checkBankSelected;
			}
		}
		if (fmArea.contains(a))
			trainFM = !trainFM;
		if (infoArea.contains(a))
			sendToURL("http://goo.gl/WEQX6");
		if (settingsArea.contains(a))
			if (!showSettings) {
				playClicked = false;
				showSettings = true;
			} else
				showSettings = false;
		if (globeSelected) {
			if (lockArea.contains(a))
				locked = !locked;
			if (logArea.contains(a))
				if (!treeSelected) {
					treeSelected = true;
					oakSelected = false;
					willowSelected = false;
					yewSelected = false;
				}
			if (oakArea.contains(a))
				if (!oakSelected) {
					treeSelected = false;
					oakSelected = true;
					willowSelected = false;
					yewSelected = false;
				}
			if (willowArea.contains(a))
				if (!willowSelected) {
					treeSelected = false;
					oakSelected = false;
					willowSelected = true;
					yewSelected = false;
				}
			if (yewArea.contains(a))
				if (!yewSelected) {
					treeSelected = false;
					oakSelected = false;
					willowSelected = false;
					yewSelected = true;
				}
			if (treeSelected) {
				if (idx1.contains(a))
					treeLocation = 1;
				if (mode1.contains(a)) {
					bankTree = false;
					dropTree = false;
				}
				if (mode2.contains(a)) {
					dropTree = true;
					bankTree = false;
				}
				if (mode3.contains(a)) {
					bankTree = true;
					dropTree = false;
				}
			}
			if (oakSelected) {
				if (idx1.contains(a))
					oakLocation = 1;
				if (idx2.contains(a))
					oakLocation = 2;
				if (idx3.contains(a))
					oakLocation = 3;
				if (mode1.contains(a)) {
					bankOak = true;
					dropOak = false;
					powerchopOak = false;
				}
				if (mode2.contains(a)) {
					dropOak = true;
					bankOak = false;
					powerchopOak = false;
				}
				if (mode3.contains(a)) {
					bankOak = false;
					dropOak = false;
					powerchopOak = true;
				}
			}
			if (willowSelected) {
				if (idx1.contains(a))
					willowLocation = 1;
				if (idx2.contains(a))
					willowLocation = 2;
				if (idx3.contains(a))
					willowLocation = 3;
				if (idx4.contains(a))
					willowLocation = 4;
				if (willowLocation == 1 || willowLocation == 2) {
					if (mode1.contains(a)) {
						bankWillow = false;
						dropWillow = false;
						powerchopWillow = false;
					}
					if (mode2.contains(a)) {
						dropWillow = false;
						bankWillow = true;
						powerchopWillow = false;
					}
					if (mode3.contains(a)) {
						bankWillow = false;
						dropWillow = true;
						powerchopWillow = false;
					}
					if (mode4.contains(a)) {
						bankWillow = false;
						dropWillow = false;
						powerchopWillow = true;
					}
				}
				if (willowLocation == 3 || willowLocation == 4) {
					if (mode1.contains(a)) {
						dropWillow = false;
						bankWillow = true;
						powerchopWillow = false;
					}
					if (mode2.contains(a)) {
						bankWillow = false;
						dropWillow = true;
						powerchopWillow = false;
					}
					if (mode3.contains(a)) {
						bankWillow = false;
						dropWillow = false;
						powerchopWillow = true;
					}
					if (!bankWillow && !powerchopWillow && !dropWillow)
						bankWillow = true;
				}
			}
			if (yewSelected) {
				if (idx1.contains(a))
					yewLocation = 1;
				if (idx2.contains(a))
					yewLocation = 2;
				if (idx3.contains(a))
					yewLocation = 3;
			}
		}
		if (showArea.contains(a))
			showPaint = !showPaint;
	}
	@Override
	public void mouseClicked(final MouseEvent e) {}
	@Override
	public void messageReceived(final MessageEvent e) {
		final String m = e.getMessage();
		if (e.getID() == MessageEvent.MESSAGE_ACTION || e.getID() == MessageEvent.MESSAGE_SERVER) {
			if (m.contains("can't light a fire"))
				findNewTile(start, false);
			if (m.contains("the ladder has been completely destroyed"))
				sawLamp = true;
			if (m.contains("You've just advanced") && !m.contains("Firemaking")) {
				clickTree = true;
			}
		}
	}
	final Color colorGreenL = new Color(40, 255, 50, 180); // back
	final Color colorGreenH = new Color(50, 255, 50, 200);
	final Color colorRed = new Color(255, 50, 50, 150); // red
	final Color colorWhiteL = new Color(255, 255, 255, 100); // White
	final Color colorBlack = new Color(0, 0, 0, 200);
	final BasicStroke stroke1 = new BasicStroke(1);
	final Font cordia = new Font("CordiaUPC", 0, 24);
	final Font arialS = new Font("Arial", 0, 9);
	final Font arialL = new Font("Arial", 0, 12);
	final Font dotum = new Font("Dotum", 0, 12);
	String dots = "";
	final BufferedImage imgFlameSelected = getImage("flameSelected.png", "http://i.imgur.com/CyNp4.png");
	final BufferedImage imgFlame = getImage("flame.png", "http://i.imgur.com/97RDO.png");
	final BufferedImage imgGlobeSelected = getImage("globeSelected.png", "http://i.imgur.com/KGZjx.png");
	final BufferedImage imgGlobe = getImage("globe.png", "http://i.imgur.com/ETx9r.png");
	final BufferedImage imgInfoSelected = getImage("infoSelected.png", "http://i.imgur.com/6HJoR.png");
	final BufferedImage imgInfo = getImage("info.png", "http://i.imgur.com/hRYtG.png");
	final BufferedImage imgPlaySelected = getImage("playSelected.png", "http://i.imgur.com/Q4Efo.png");
	final BufferedImage imgPlay = getImage("play.png", "http://i.imgur.com/CPp7m.png");
	final BufferedImage imgTickSelected = getImage("tickSelected.png", "http://i.imgur.com/mBUvC.png");
	final BufferedImage imgTick = getImage("tick.png", "http://i.imgur.com/fwlC1.png");
	final BufferedImage imgTickSmall = getImage("tickSmall.png", "http://i.imgur.com/0J7me.png");
	final BufferedImage imgSettingsSelected = getImage("settingsSelected.png", "http://i.imgur.com/ZErbl.png");
	final BufferedImage imgSettings = getImage("settings.png", "http://i.imgur.com/fFnrl.png");
	final BufferedImage imgCursor = getImage("cursor.png", "http://i.imgur.com/OLnoP.png");
	final BufferedImage imgCursorSelected = getImage("cursorSelected.png", "http://i.imgur.com/GpbLy.png");
	final BufferedImage imgLockOpen = getImage("lockOpen.png", "http://i.imgur.com/Wn8M8.png");
	final BufferedImage imgLockClosed = getImage("lockClosed.png", "http://i.imgur.com/W4EhD.png");
	final BufferedImage optionsBackground = getImage("optionsBackground.png", "http://i.imgur.com/3L58P.png");
	boolean treeSelected = true;
	boolean oakSelected = false;
	boolean willowSelected = false;
	boolean yewSelected = false;
	boolean globeSelected = false;
	boolean infoSelected = false;
	boolean playSelected = false;
	boolean playClicked = false;
	boolean settingsSelected = false;
	boolean showSettings = true;
	boolean locked = false;
	boolean checkBankSelected = false;
	boolean yewsAfter60Selected = false;
	boolean useAvailableHatchetsSelected = false;
	int treeLocation = 1;
	int oakLocation = 1;
	int willowLocation = 1;
	int yewLocation = 1;
	long hours = 0;
	long minutes = 0;
	long seconds = 0;
	public void onRepaint(final Graphics g1) {
		final Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		long millis = System.currentTimeMillis() - startTime;
		hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		seconds = millis / 1000;
		if (isLoggedIn()) {
			if (globeSelected) {
				if (treeSelected) {
					g.setColor(colorBlack); // Largest box
					g.fillRect(336, 3, 124, 25 + 20 * 1);
					g.setColor(Color.BLACK);
					g.drawRect(336, 3, 124, 25 + 20 * 1);
					g.setColor(colorBlack); // Thin middle box
					g.fillRect(460, 3, 27, 25 + 20 * 1);
					g.setColor(Color.BLACK);
					g.drawRect(460, 3, 27, 25 + 20 * 1);
					g.drawImage(imgTickSmall, 466, 32 + 20 * (treeLocation - 1), null); // Tick
					g.setColor(colorGreenH);
					g.setFont(dotum);
					g.drawString("Lumbridge", 341, 44);
				}
				if (oakSelected) {
					g.setColor(colorBlack); // Largest box
					g.fillRect(336, 3, 124, 25 + 20 * 3);
					g.setColor(Color.BLACK);
					g.drawRect(336, 3, 124, 25 + 20 * 3);
					g.setColor(colorBlack); // Thin middle box
					g.fillRect(460, 3, 27, 25 + 20 * 3);
					g.setColor(Color.BLACK);
					g.drawRect(460, 3, 27, 25 + 20 * 3);
					g.drawImage(imgTickSmall, 466, 32 + 20 * (oakLocation - 1), null); // Tick
					g.setColor(colorGreenH);
					g.setFont(dotum);
					g.drawString("North East Draynor", 341, 44);
					g.drawString("East Draynor", 341, 64);
					g.drawString("North Draynor", 341, 84);
				}
				if (willowSelected) {
					g.setColor(colorBlack); // Largest box
					g.fillRect(336, 3, 124, 25 + 20 * 4);
					g.setColor(Color.BLACK);
					g.drawRect(336, 3, 124, 25 + 20 * 4);
					g.setColor(colorBlack); // Thin middle box
					g.fillRect(460, 3, 27, 25 + 20 * 4);
					g.setColor(Color.BLACK);
					g.drawRect(460, 3, 27, 25 + 20 * 4);
					g.drawImage(imgTickSmall, 466, 32 + 20 * (willowLocation - 1), null); // Tick
					g.setColor(colorGreenH);
					g.setFont(dotum);
					g.drawString("Rimmington", 341, 44);
					g.drawString("Lumbridge", 341, 64);
					g.drawString("Port Sarim", 341, 84);
					g.drawString("Draynor", 341, 104);
				}
				if (yewSelected) {
					g.setColor(colorBlack); // Largest box
					g.fillRect(336, 3, 124, 25 + 20 * 3);
					g.setColor(Color.BLACK);
					g.drawRect(336, 3, 124, 25 + 20 * 3);
					g.setColor(colorBlack); // Thin middle box
					g.fillRect(460, 3, 27, 25 + 20 * 3);
					g.setColor(Color.BLACK);
					g.drawRect(460, 3, 27, 25 + 20 * 3);
					g.drawImage(imgTickSmall, 466, 32 + 20 * (yewLocation - 1), null); // Tick
					g.setColor(colorGreenH);
					g.setFont(dotum);
					g.drawString("Rimmington", 341, 44);
					g.drawString("Lumbridge", 341, 64);
					g.drawString("Port Sarim", 341, 84);
				}
				if (treeSelected) {
					g.setColor(colorGreenH);
					g.setFont(arialS);
					g.drawString("Tree", 342, 21);
					if (treeLocation == 1) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						if (bankTree)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 81);
						if (dropTree)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (!bankTree && !dropTree)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Sell", 290, 41);
					}
				} else {
					g.setFont(arialS);
					g.setColor(Color.WHITE);
					g.drawString("Tree", 342, 21);
				}
				if (oakSelected) {
					g.setColor(colorGreenH);
					g.setFont(arialS);
					g.drawString("Oak", 374, 21);
					if (oakLocation == 1) { // All modes for oaks are the same.
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						g.setColor(Color.WHITE);
						if (bankOak || !dropOak && !powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 41);
						if (dropOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 81);
					}
					if (oakLocation == 2) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						if (bankOak || !dropOak && !powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 41);
						if (dropOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 81);
					}
					if (oakLocation == 3) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						if (bankOak || !dropOak && !powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 41);
						if (dropOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (powerchopOak)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 81);
					}
				} else {
					g.setColor(Color.WHITE);
					g.setFont(arialS);
					g.drawString("Oak", 374, 21);
				}
				if (willowSelected) {
					g.setColor(colorGreenH);
					g.setFont(arialS);
					g.drawString("Willow", 401, 21);
					if (willowLocation == 1) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 80);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 80);
						if (!bankWillow && !dropWillow && !powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Sell", 290, 41);
						if (bankWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 61);
						if (dropWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 81);
						if (powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 101);
					}
					if (willowLocation == 2) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 80);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 80);
						if (!bankWillow && !dropWillow && !powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Sell", 290, 41);
						if (bankWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 61);
						if (dropWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 81);
						if (powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 101);
					}
					if (willowLocation == 3) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						if (bankWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 41);
						if (dropWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 81);
					}
					if (willowLocation == 4) {
						g.setColor(colorBlack);
						g.fillRect(285, 28, 51, 60);
						g.setColor(Color.BLACK);
						g.drawRect(285, 28, 51, 60);
						if (bankWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Deposit", 290, 41);
						if (dropWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Drop", 290, 61);
						if (powerchopWillow)
							g.setColor(colorGreenH);
						else
							g.setColor(Color.WHITE);
						g.drawString("Powerchop", 290, 81);
					}
				} else {
					g.setColor(Color.WHITE);
					g.setFont(arialS);
					g.drawString("Willow", 401, 21);
				}
				if (yewSelected) {
					g.setColor(colorGreenH);
					g.setFont(arialS);
					g.drawString("Yew", 435, 21);
					g.setColor(colorBlack);
					g.fillRect(285, 28, 51, 20);
					g.setColor(colorBlack);
					g.drawRect(285, 28, 51, 20);
					if (yewLocation == 1) { // All yew modes are the same.
						g.setColor(colorGreenH);
						g.drawString("Deposit", 290, 41);
					}
					if (yewLocation == 2) {
						g.setColor(colorGreenH);
						g.drawString("Deposit", 290, 41);
					}
					if (yewLocation == 3) {
						g.setColor(colorGreenH);
						g.drawString("Deposit", 290, 41);
					}
				} else {
					g.setColor(Color.WHITE);
					g.setFont(arialS);
					g.drawString("Yew", 435, 21);
				}
				g.setColor(Color.BLACK);
				g.drawRect(285, 3, 51, 25);
				g.setColor(colorBlack);
				g.fillRect(285, 3, 51, 25);
				g.setColor(Color.WHITE);
				g.drawString("MODE", 290, 21);
				g.setColor(Color.BLACK);
				g.drawRect(336, 3, 31, 25);
				g.setColor(Color.BLACK);
				g.drawRect(367, 3, 31, 25);
				g.setColor(Color.BLACK);
				g.drawRect(398, 3, 31, 25);
				g.setColor(Color.BLACK);
				g.drawRect(429, 3, 31, 25);
				g.setFont(arialS);
			}
			if (globeSelected) {
				g.drawImage(imgGlobeSelected, 490, 5, null); // Globe
				if (!locked)
					g.drawImage(imgLockOpen, 462, 5, null);
				else
					g.drawImage(imgLockClosed, 462, 5, null);
			} else
				g.drawImage(imgGlobe, 490, 5, null);
			if (trainFM || trainFMHovered)
				g.drawImage(imgFlameSelected, 490, 32, null); // Flame
			else
				g.drawImage(imgFlame, 490, 32, null);
			if (infoSelected)
				g.drawImage(imgInfoSelected, 490, 59, null);
			else
				g.drawImage(imgInfo, 490, 59, null);
			if (settingsSelected)
				g.drawImage(imgSettingsSelected, 490, 86, null);
			else
				g.drawImage(imgSettings, 490, 86, null);
			if (!playClicked) {
				if (showSettings) {
					g.drawImage(optionsBackground, 0, 338, null);
					g.setFont(dotum);
					g.setColor(Color.BLACK);
					g.drawString("Cut Yews after 60 WC.", 338, 362);
					if (yewsAfter60Selected)
						g.drawImage(imgTickSelected, 469, 345, null);
					else
						g.drawImage(imgTick, 469, 345, null);
					g.drawString("Buy my own hatchets.", 341, 412);
					g.drawString("Use hatchets in bank or inventory.", 273, 442);
					if (useAvailableHatchetsSelected) {
						g.drawImage(imgTickSelected, 469, 425, null);
						g.drawImage(imgTick, 469, 395, null);
					} else {
						g.drawImage(imgTickSelected, 469, 395, null);
						g.drawImage(imgTick, 469, 425, null);
					}
					g.drawString("Force one bank.", 10, 362);
					if (checkBankSelected)
						g.drawImage(imgTickSelected, 200, 345, null);
					else
						g.drawImage(imgTick, 200, 345, null);
				}
				if (playSelected)
					g.drawImage(imgPlaySelected, 240, 150, null);
				else
					g.drawImage(imgPlay, 240, 150, null);
			}
			mp.drawPaint(g);
		}
		if (playClicked)
			drawMouse(g);
	}

	class Paint {
		long start;
		int x = 550;
		int y = 327;
		int w = 176 + 10;
		int h = 111 + 15; // TODO
		int xLoc = x;
		int yLoc = y;
		Rectangle moveBox = new Rectangle(x, y, 176, 96);
		public void drawPaint(Graphics g1) {
			if (mp.x < 0)
				mp.x = 0;
			if (mp.y < 0)
				mp.y = 0;
			if (mp.x > 764 - w)
				mp.x = 764 - w;
			if (mp.y > 502 - h)
				mp.y = 502 - h;
			moveBox = new Rectangle(x, y, 176, 96);
			final Graphics2D g = (Graphics2D) g1;
			if (showPaint) {
				g.setColor(colorGreenL);
				g.fillRect(x, y, w, h); // Green back
				g.setColor(Color.BLACK);
				g.setStroke(stroke1);
				g.drawRect(x, y, w, h);
				g.setColor(colorRed); // Red bar
				g.fillRect(x, y - 21, w, 18);
				g.setColor(Color.BLACK);
				g.drawRect(x, y - 21, w, 18);
				g.setColor(colorGreenH); // Green bar
				g.fillRect(x + 2, y - 19 + 1, skills.getPercentToNextLevel(Skills.WOODCUTTING) * (w - 1) / 100, 15 - 2);
				g.setColor(colorWhiteL); // White bar
				g.fillRect(x + 1, y - 12, w - 1, 9);
				g.setFont(arialL);
				g.setColor(Color.BLACK);
				drawCenteredString(Integer.toString(wcLvl()) + " WC", x, y - 20, w, 18, g);
				if (trainFM) {
					g.setColor(colorGreenL);
					g.fillRect(x, y - 73, w, 49); // Green back
					g.setColor(Color.BLACK);
					g.setStroke(stroke1);
					g.drawRect(x, y - 73, w, 49);
					g.setColor(colorRed); // Red bar
					g.fillRect(x, y - 94, w, 18);
					g.setColor(Color.BLACK);
					g.drawRect(x, y - 94, w, 18);
					g.setColor(colorGreenH); // Green bar
					g.fillRect(x + 2, y - 91, skills.getPercentToNextLevel(Skills.FIREMAKING) * (w - 1) / 100, 13);
					g.setColor(colorWhiteL); // White bar
					g.fillRect(x + 1, y - 85, w - 1, 9);
					g.setFont(arialL);
					g.setColor(Color.BLACK);
					drawCenteredString(Integer.toString(fmLvl()) + " FM", x, y - 76 - 17, w, 18, g);
					g.drawString(fmLvl() - initialLevel2 + " levels, "
					        + (skills.getCurrentExp(Skills.FIREMAKING) - initialXP2) + " xp", x + 5, y - 42 - 17);
					g.drawString(
					    (double) Math.round((skills.getCurrentExp(Skills.FIREMAKING) - initialXP2) * 3600D
					            / (System.currentTimeMillis() - startTime) * 10)
					            / 10
					            + "k xp/hr  "
					            + (double) Math.round((skills.getCurrentExp(Skills.FIREMAKING)
					                    + skills.getCurrentExp(Skills.WOODCUTTING) - initialXP - initialXP2)
					                    * 3600D / (System.currentTimeMillis() - startTime) * 10) / 10 + "k total xp/hr",
					    x + 5, y - 27 - 17);
					double ttlFm = Math.round((double) (skills.getExpToNextLevel(Skills.FIREMAKING))
					        / (double) (skills.getCurrentExp(Skills.FIREMAKING) - initialXP2)
					        * (double) (System.currentTimeMillis() - startTime) / 36000) / 10d;
					if (ttlFm > 1000)
						ttlFm = 0;
					g.drawString(ttlFm + " hours until " + (fmLvl() + 1) + " fm.", x + 5, y - 27 - 2);
					if (burnLogs) {
						g.setColor(Color.WHITE);
						long timeRemaining = timer2 - System.currentTimeMillis();
						if (timeRemaining >= 0)
							g.drawString(Long.toString(timeRemaining), 255, 180);
					}
				}
				g.setFont(cordia);
				g.setColor(new Color(20, 20, 20, 100));
				g.drawString("Dynamic Woodcutter", x + 5, y + 17);
				g.setColor(Color.BLACK);
				g.drawString("Dynamic Woodcutter", x + 6, y + 18);
				g.setFont(arialS);
				g.setColor(Color.BLACK);
				g.drawString("v" + scriptVersion, x + 156, y + 18);
				g.setFont(arialL); // General text
				if (status != null && !status.contains("unavailable"))
					g.drawString("Status: " + status, x + 5, y + 33);
				else {
					g.setColor(Color.RED);
					g.drawString("Status: " + status, x + 5, y + 33);
					g.setColor(Color.BLACK);
				}
				DecimalFormat a = new DecimalFormat("00");
				g.drawString("Time: " + a.format(hours) + ":" + a.format(minutes) + ":" + a.format(seconds), x + 5, y + 48);
				if (totalCash > 1000000)
					g.drawString("Available wealth: " + Double.toString(Math.round(totalCash / 100000) / 10d) + "m",
					    x + 5, y + 63);
				else if (totalCash > 1000)
					g.drawString("Available wealth: " + Double.toString(Math.round(totalCash / 100) / 10d) + "k",
					    x + 5, y + 63);
				else
					g.drawString("Available wealth: " + Integer.toString(totalCash), x + 5, y + 63);
				g.drawString(wcLvl() - initialLevel + " levels and "
				        + (skills.getCurrentExp(Skills.WOODCUTTING) - initialXP) + " xp gained.", x + 5, y + 78);
				double ttlWc = Math.round((double) (skills.getExpToNextLevel(Skills.WOODCUTTING))
				        / (double) (skills.getCurrentExp(Skills.WOODCUTTING) - initialXP)
				        * (double) (System.currentTimeMillis() - startTime) / 36000) / 10d;
				if (ttlWc > 1000) {
					ttlWc = 0;
				}
				g.drawString(
				    (double) Math.round((skills.getCurrentExp(Skills.WOODCUTTING) - initialXP) * 3600D
				            / (System.currentTimeMillis() - startTime) * 10)
				            / 10 + "k exp/hour.", x + 5, y + 93);
				g.drawString(ttlWc + " hours until " + (wcLvl() + 1) + " wc.", x + 5, y + 108);
				if (antiBan.length() > 0) {
					g.setFont(arialS);
					g.setColor(colorWhiteL);
					g.fillRect(100, 323, g.getFontMetrics().stringWidth("Antiban: " + antiBan) + 3, 15);
					g.setColor(Color.BLACK);
					g.drawRect(100, 323, g.getFontMetrics().stringWidth("Antiban: " + antiBan) + 3, 15);
					g.drawString("Antiban: " + antiBan, 102, 333);
				}
				g.setFont(arialL);
				g.setColor(Color.BLACK); // Hide button
				g.drawRect(x + w - 34, y + h - 19, 34, 19);
				g.setFont(arialL);
				g.setColor(Color.BLACK);
				g.drawString("Hide", x + w - 29, y + h - 4);
			} else {
				g.setColor(colorGreenL); // Show button TODO show/hide area needs to be changed.
				g.fillRect(x + w - 34, y + h + -19, 34, 19);
				g.setColor(Color.BLACK);
				g.drawRect(x + w - 34, y + h - 19, 34, 19);
				g.setFont(arialL);
				g.setColor(Color.BLACK);
				g.drawString("Show", x + w - 32, y + h - 4);
			}
		}
		public void drawCenteredString(String s, int x, int y, int w, int h, Graphics g) {
			FontMetrics fm = g.getFontMetrics();
			int xx = x + (w - fm.stringWidth(s)) / 2;
			int yy = y + (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
			g.drawString(s, xx, yy);
		}
	}
	private void drawMouse(final Graphics2D g) {
		g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
		final Point p = mouse.getLocation();
		while (!mousePath.isEmpty() && mousePath.peek().isUp())
			mousePath.remove();
		final MousePathPoint mpp = new MousePathPoint(p.x, p.y, 1000); // 1000 = lasting time/MS
		if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
			mousePath.add(mpp);
		MousePathPoint lastPoint = null;
		for (final MousePathPoint a : mousePath) {
			if (lastPoint != null) {
				g.setColor(a.getColor());
				g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
			}
			lastPoint = a;
		}
		final long mpt = System.currentTimeMillis() - mouse.getPressTime();
		if (mpt < 100 || mouse.isPressed()) {
			clickTimer = System.currentTimeMillis();
			g.drawImage(imgCursorSelected, p.x, p.y, null);
		} else {
			final double fadeTime = 800;
			final double timeDiff = fadeTime - (System.currentTimeMillis() - clickTimer);
			final float alpha = (float) (timeDiff / fadeTime);
			g.drawImage(imgCursor, p.x, p.y, null);
			if (timeDiff <= fadeTime && timeDiff >= 0) { // alpha value [0.0, 1.0]
				g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
				g.drawImage(imgCursorSelected, p.x, p.y, null);
			}
		}
	}
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();

	@SuppressWarnings("serial")
	private class MousePathPoint extends Point { // Enfilade.
		private final long finishTime;
		private final double lastingTime;
		private int toColor(final double d) {
			return Math.min(255, Math.max(0, (int) d));
		}
		public MousePathPoint(final int x, final int y, final int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}
		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}
		public Color getColor() {
			return new Color(0, 100, 255, toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));
		}
	}
	int logCount = 0;
	RSItem invLog;
	boolean yewAfter60 = false;
	boolean clickTree = false;
	boolean powerchop = false;
	boolean drop = false;
	boolean dropTree = false;
	boolean bankTree = false;
	boolean bankOak = false;
	boolean dropOak = false;
	boolean powerchopOak = false;
	boolean bankWillow = false;
	boolean dropWillow = false;
	boolean powerchopWillow = false;
	boolean useAvailableHatchets = false;
	boolean checkedBank = false;
	boolean hasBronzeHatchet = false;
	boolean hasIronHatchet = false;
	boolean hasSteelHatchet = false;
	boolean hasBlackHatchet = false;
	boolean hasMithrilHatchet = false;
	boolean hasAdamantHatchet = false;
	boolean hasRuneHatchet = false;
	int bestHatchetAvailable = -1;
	public final int CLERKS = 2593; // 2241, 2240, 2593, 1419
	public final int BANKERS = 3416; // 3293, 3416, 2718, 3418
	public final int GE_INTERFACE = 105;
	public final int GE_CLOSE = 14;
	public final int SEARCH = 389;
	public final int COLLECT_INTERFACE = 109;
	public int SLOT = 0;
	private final Pattern PATTERN = Pattern.compile("(?i)<td><img src=\".+obj_sprite\\.gif\\?id=(\\d+)\" alt=\"(.+)\"");
	private final String HOST = "http://services.runescape.com";
	private final String GET = "/m=itemdb_rs/viewitem.ws?obj=";
	/**
	 * Buys items from the Grand Exchange if it's open
	 * @param itemName item to buy
	 * @param slotNumber slot number to buy from (1-5)
	 * @param quantity amount to buy
	 * @param price Price to buy from. 0 will leave it as default price
	 * @return <tt>true</tt> if bought successfully; otherwise <tt>false</tt>
	 */
	public boolean buy(String itemName, final int slotNumber, final int quantity, final int price) {
		SLOT = slotNumber;
		itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1).toLowerCase();
		final String Sep[] = itemName.split(" ");
		String searchName = null;
		for (int i = 0; i < Sep.length;) {
			if (!Sep[i].contains("("))
				if (searchName == null)
					searchName = Sep[i].toLowerCase(Locale.ENGLISH);
				else
					searchName += " " + Sep[i].toLowerCase(Locale.ENGLISH);
			i++;
		}
		if (slotNumber == 0 || slotNumber > 5)
			return false;
		if (isOpen()) {
			final GEBuyMethods t = new GEBuy(slotNumber);
			final int buyClick = t.getBuyClick();
			if (!isSearching()) {
				interfaces.getComponent(GE_INTERFACE, buyClick).doClick();
				sleep(random(1500, 2000));
			}
			if (isSearching() && !hasSearched(searchName)) {
				keyboard.sendText(searchName, true);
				sleep(random(1000, 1500));
			}
			if (isSearching() && hasSearched(searchName)) {
				boolean foundItem = false;
				if (findItem() && !foundItem) {
					boolean done = false;
					int index = 0;
					if (!done)
						for (int i = 0; interfaces.getComponent(389, 4).getComponent(i) != null;) {
							if (interfaces.getComponent(389, 4).getComponent(i).getText().equals(itemName))
								index = i;
							i++;
							if (interfaces.getComponent(389, 4).getComponent(i + 1) == null)
								done = true;
						}
					if (done && index == 0)
						return false;
					if (done && index > 0) {
						if (!interfaces.getComponent(389, 4).getComponent(index).isValid()
						        && interfaces.getComponent(389, 8).getComponent(5).isValid()) {
							interfaces.getComponent(389, 8).getComponent(5).doClick();
							sleep(random(200, 500));
						}
						if (interfaces.getComponent(389, 4).getComponent(index).isValid()) {
							interfaces.getComponent(389, 4).getComponent(index).doClick();
							sleep(random(700, 900));
							foundItem = true;
						}
					}
				}
				if (foundItem) {
					boolean changeQuantity = true;
					boolean changePrice;
					changePrice = price > 1;
					int times = 0;
					while (changeQuantity) {
						if (times >= 3) {
							close();
							return false;
						}
						if (isOpen()) {
							if (interfaces.getComponent(GE_INTERFACE, 148).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 148).getText().contains(
							            "" + formatNumb(quantity))) {
								changeQuantity = false;
								break;
							}
							if (interfaces.getComponent(GE_INTERFACE, 168).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 168).getText().contains("...")) {
								interfaces.getComponent(GE_INTERFACE, 168).doClick();
								sleep(random(1000, 1200));
							}
							if (interfaces.getComponent(752, 4).getText().contains("you wish to purchase"))
								keyboard.sendText("" + quantity, true);
							sleep(random(1000, 2000));
							if (interfaces.getComponent(GE_INTERFACE, 148).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 148).getText().contains(
							            "" + formatNumb(quantity))) {
								changeQuantity = false;
								sleep(random(1000, 1200));
							}
						} else
							return false;
						times++;
					}
					times = 0;
					while (changePrice && !changeQuantity) {
						if (times >= 3) {
							close();
							return false;
						}
						if (isOpen()) {
							if (interfaces.getComponent(GE_INTERFACE, 153).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 153).getText().contains(
							            "" + formatNumb(price))) {
								changePrice = false;
								break;
							}
							if (interfaces.getComponent(GE_INTERFACE, 177).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 177).getText().contains("...")) {
								interfaces.getComponent(GE_INTERFACE, 177).doClick();
								sleep(random(1000, 1200));
							}
							if (interfaces.getComponent(752, 4).getText().contains("you wish to buy"))
								keyboard.sendText("" + price, true);
							sleep(random(1000, 2000));
							if (interfaces.getComponent(GE_INTERFACE, 153).getText() != null
							        && interfaces.getComponent(GE_INTERFACE, 153).getText().contains(
							            "" + formatNumb(price))) {
								changePrice = false;
								sleep(random(1000, 1200));
							}
						} else
							return false;
						times++;
					}
					if (!changePrice && !changeQuantity) {
						if (interfaces.getComponent(GE_INTERFACE, 187).getText() != null)
							interfaces.getComponent(GE_INTERFACE, 187).doClick();
						close();
						sentOffer = true;
						return true;
					}
				}
			}
			close();
			return false;
		}
		return false;
	}
	// Credits to Dwarfeh for most of the Grand Exchange methods :)
	/**
	 * Sells items to the Grand Exchange if it's open
	 * @param itemName item to sell
	 * @param slotNumber slot number to sell from (1-5)
	 * @param quantity amount to sell
	 * @param price Price to sell from. 0 will leave it as default price
	 * @return <tt>true</tt> if sold successfully; otherwise <tt>false</tt>
	 */
	public boolean sell(final String itemName, final int slotNumber, final int quantity, final int price) {
		SLOT = slotNumber;
		if (slotNumber == 0 || slotNumber > 5)
			return false;
		if (!inventory.contains(itemName))
			return false;
		if (isOpen()) {
			final GEBuyMethods t = new GEBuy(slotNumber);
			final int sellClick = t.getSellClick();
			boolean offerItem = false;
			boolean offeredItem = false;
			if (!isSelling()) {
				interfaces.getComponent(GE_INTERFACE, sellClick).doClick();
				sleep(random(700, 900));
				offerItem = true;
			}
			if (!isSelling() && offerItem) {
				inventory.getItem(itemName).doClick(true);
				sleep(random(500, 700));
				offeredItem = true;
			}
			if (isSelling()) {
				close();
				return false;
			}
			if (!isSelling() && offeredItem) {
				boolean changeQuantity;
				changeQuantity = quantity > 1;
				boolean changePrice;
				changePrice = price > 1;
				int times = 0;
				while (changeQuantity) {
					if (times == 3) {
						close();
						return false;
					}
					if (isOpen()) {
						if (interfaces.getComponent(GE_INTERFACE, 148).getText() != null
						        && interfaces.getComponent(GE_INTERFACE, 148).getText().contains(
						            "" + formatNumb(quantity))) {
							changeQuantity = false;
							break;
						}
						if (interfaces.getComponent(GE_INTERFACE, 168).getText() != null
						        && interfaces.getComponent(GE_INTERFACE, 168).getText().contains("...")) {
							interfaces.getComponent(GE_INTERFACE, 168).doClick();
							sleep(random(700, 900));
						}
						if (interfaces.getComponent(752, 4).getText().contains("amount you wish to"))
							keyboard.sendTextInstant("" + quantity, true);
						sleep(random(1000, 2000));
						if (interfaces.getComponent(GE_INTERFACE, 148).getText() != null
						        && interfaces.getComponent(GE_INTERFACE, 148).getText().contains(
						            "" + formatNumb(quantity)))
							changeQuantity = false;
					} else
						return false;
					times++;
				}
				times = 0;
				while (changePrice && !changeQuantity) {
					if (times == 3) {
						close();
						return false;
					}
					if (isOpen()) {
						if (interfaces.getComponent(GE_INTERFACE, 177).getText() != null
						        && interfaces.getComponent(GE_INTERFACE, 168).getText().contains("...")) {
							interfaces.getComponent(GE_INTERFACE, 177).doClick();
							sleep(random(700, 900));
						}
						if (interfaces.getComponent(752, 4).getText().contains("you wish to sell"))
							keyboard.sendTextInstant("" + price, true);
						sleep(random(1000, 2000));
						if (interfaces.getComponent(GE_INTERFACE, 153).getText() != null
						        && interfaces.getComponent(GE_INTERFACE, 153).getText().contains("" + formatNumb(price)))
							changePrice = false;
					} else
						return false;
					times++;
				}
				if (!changeQuantity && !changePrice)
					if (interfaces.getComponent(GE_INTERFACE, 187).getText() != null)
						interfaces.getComponent(GE_INTERFACE, 187).doClick();
				close();
				offeredOaks = true;
				return true;
			}
			close();
			return false;
		}
		return false;
	}
	/**
	 * @return <tt>true</tt> if selling; otherwise <tt>false</tt>
	 */
	public boolean isSelling() {
		return interfaces.getComponent(GE_INTERFACE, 142).isValid()
		        && !interfaces.getComponent(GE_INTERFACE, 142).getText().equals("Choose an item to exchange");
	}
	/**
	 * Determines membership
	 * @return <tt>true</tt> if members is selected for the account; otherwise <tt>false</tt>
	 */
	public boolean isMember() {
		return AccountManager.isMember(account.getName());
	}
	/**
	 * Sets the number format as the same as GrandExchange's
	 * @param money GrandExchange's money
	 * @return number to match GrandExchange's
	 */
	public static String formatNumb(final long money) {
		return new DecimalFormat("###,###,###,###,###,###").format(money);
	}
	/**
	 * Gets the total slots there are for the person
	 * @return number of slots if account is member
	 */
	public int getTotalSlots() {
		return isMember() ? 6 : 2;
	}
	/**
	 * Checks to see if the GE slot is empty
	 * @param slot gets the correct interface
	 * @return <tt>true</tt> if empty; otherwise <tt>false</tt>
	 */
	public boolean isSlotEmpty(final int slot) {
		final GEBuyMethods check2 = new GEBuy(slot);
		final int check = check2.getInterface();
		if (isOpen())
			if (interfaces.getComponent(GE_INTERFACE, check).getComponent(10).getText().equals("Empty"))
				return true;
		return false;
	}
	/**
	 * Will determine the total amount of empty slots
	 * @return total amount of empty slots
	 */
	public int getAllEmptySlots() {
		if (isOpen()) {
			int total = 0;
			for (int i = 1; i <= getTotalSlots();) {
				SLOT = i;
				final GEBuyMethods check2 = new GEBuy(i);
				final int check = check2.getInterface();
				if (interfaces.getComponent(GE_INTERFACE, check).getComponent(10).getText().equals("Empty"))
					total++;
				if (i == getTotalSlots())
					return total;
				i++;
			}
		}
		return 0;
	}
	/**
	 * Checks for nearest empty slot
	 * @return an empty spot in the GE if there is one. If not, 0
	 */
	public int getEmptySlot() {
		if (isOpen()) {
			for (int i = 1; i <= getTotalSlots();) {
				SLOT = i;
				final GEBuyMethods check2 = new GEBuy(i);
				final int check = check2.getInterface();
				if (interfaces.getComponent(GE_INTERFACE, check).getComponent(10).getText().equals("Empty"))
					return i;
				i++;
			}
			return 0;
		}
		return 0;
	}
	/**
	 * Determines if an offer has completed or not
	 * @return <tt>true</tt> if an offer is completed; otherwise <tt>false</tt>
	 */
	public boolean isAnOfferCompleted() {
		final GEBuyMethods check2 = new GEBuy();
		return check2.isAnOfferCompleted();
	}
	/**
	 * Determines if there is an item by the name
	 * @return <tt>true</tt> if an item was found; otherwise <tt>false</tt>
	 */
	public boolean findItem() {
		return interfaces.getComponent(SEARCH, 4).getComponent(0) != null
		        && interfaces.getComponent(SEARCH, 4).getComponent(0).getText() != null
		        && !interfaces.getComponent(SEARCH, 4).getComponent(0).getText().equals("No matching items found.");
	}
	/**
	 * Determines if the person has searched or not
	 * @param itemName is defined by buy/sell item
	 * @return <tt>true</tt> if they have searched; otherwise <tt>false</tt>
	 */
	public boolean hasSearched(final String itemName) {
		if (!Character.isUpperCase(itemName.charAt(0)))
			Character.toUpperCase(itemName.charAt(0));
		return interfaces.getComponent(GE_INTERFACE, 142).getText().contains(itemName)
		        || interfaces.getComponent(SEARCH, 4).getComponent(1) != null
		        && interfaces.getComponent(SEARCH, 9).getText().contains(itemName);
	}
	/**
	 * Determines if the player is searching
	 * @return <tt>true</tt> if interface is valid; otherwise <tt>false</tt>
	 */
	public boolean isSearching() {
		return interfaces.getComponent(GE_INTERFACE, 134).isValid();
	}
	/**
	 * Checks whether the GE is open
	 * @return <tt>true</tt> if the GE interface is valid; otherwise <tt>false</tt>
	 */
	public boolean isOpen() {
		return getInterface().isValid();
	}
	/**
	 * Gets the bank's interface.
	 * @return <tt>true</tt> if interface is valid
	 */
	public RSInterface getInterface() {
		return interfaces.get(GE_INTERFACE);
	}
	/**
	 * Gets the general interface for the slot
	 * @param slot determines which one to take from
	 * @return interface for the slot
	 */
	public int bankGetInterface(final int slot) {
		final BankCollectMethods collect = new BankCollect(slot);
		return collect.getBankInterface();
	}
	/**
	 * Gets the left interface for the slot
	 * @param slot determines which one to take from
	 * @return left interface for the slot
	 */
	public int bankGetLeftInterface(final int slot) {
		final BankCollectMethods collect = new BankCollect(slot);
		return collect.getBankLeftCollect();
	}
	/**
	 * Gets the right interface for the slot
	 * @param slot determines which one to take from
	 * @return right interface for the slot
	 */
	public int bankGetRightInterface(final int slot) {
		final BankCollectMethods collect = new BankCollect(slot);
		return collect.getBankRightCollect();
	}
	/**
	 * Collects everything from the interface
	 * @return <tt>true</tt> if collected all successfully; otherwise <tt>false</tt>
	 */
	public boolean bankCollectAll() {
		final BankCollectMethods collect = new BankCollect();
		return collect.bankCollectAll();
	}
	/**
	 * Opens collection interface
	 * @return <tt>true</tt> if opened successfully; otherwise <tt>false</tt>
	 */
	public boolean bankCollectOpen() {
		final BankCollectMethods collect = new BankCollect();
		return collect.bankOpen();
	}
	/**
	 * Closes collection interface
	 * @return <tt>true</tt> if closed successfully; otherwise <tt>false</tt>
	 */
	public boolean bankCollectClose() {
		final BankCollectMethods collect = new BankCollect();
		return collect.bankClose();
	}
	/**
	 * Checks collection interface
	 * @return <tt>true</tt> if opened; otherwise <tt>false</tt>
	 */
	public boolean bankCollectIsOpen() {
		final BankCollectMethods collect = new BankCollect();
		return collect.bankIsOpen();
	}
	/**
	 * Closes the GE.
	 * @return <tt>true</tt> if the GE is no longer open; otherwise <tt>false</tt>
	 */
	public boolean close() {
		if (isOpen()) {
			interfaces.getComponent(GE_INTERFACE, GE_CLOSE).doClick();
			sleep(random(1500, 2000));
			return !isOpen();
		}
		return !isOpen();
	}
	/**
	 * Opens the GrandExchange
	 * @return <tt>true</tt> if open; otherwise <tt>false</tt>
	 */
	public boolean open() {
		if (!isOpen()) {
			final RSNPC i = npcs.getNearest(CLERKS);
			if (!i.isValid())
				return false;
			mouse.move(i.getPoint());
			mouse.click(false);
			// i.interact("Exchange ") // TODO Clicked Talk-to
			if (menu.clickIndex(menu.getIndex("Exchange") + 1)) {
				chill();
				for (int j = 0; j < 10 && !isOpen(); j++)
					sleep(random(100, 200));
				sleep(random(700, 900));
				return isOpen();
			} else
				return false;
		}
		return true;
	}
	/**
	 * Gets the name of the given item ID. Should not be used.
	 * @param itemID The item ID to look for.
	 * @return The name of the given item ID or an empty String if unavailable.
	 * @see GrandExchange#lookup(int)
	 */
	public String getItemName(final int itemID) {
		final GEItem geItem = lookup(itemID);
		if (geItem != null)
			return geItem.getName();
		return "";
	}
	/**
	 * Gets the ID of the given item name. Should not be used.
	 * @param itemName The name of the item to look for.
	 * @return The ID of the given item name or -1 if unavailable.
	 * @see GrandExchange#lookup(java.lang.String)
	 */
	public int getItemID(final String itemName) {
		final GEItem geItem = lookup(itemName);
		if (geItem != null)
			return geItem.getID();
		return -1;
	}
	/**
	 * Collects data for a given item ID from the Grand Exchange website.
	 * @param itemID The item ID.
	 * @return An instance of GrandExchange.GEItem; <code>null</code> if unable to fetch data.
	 */
	public GEItem lookup(final int itemID) {
		try {
			final URL url = new URL(HOST + GET + itemID);
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			boolean exists = false;
			int i = 0;
			final double[] values = new double[4];
			String name = "", examine = "";
			while ((input = br.readLine()) != null)
				if (input.contains("<div class=\"brown_box main_ge_page") && !exists) {
					if (!input.contains("vertically_spaced"))
						return null;
					exists = true;
					br.readLine();
					br.readLine();
					name = br.readLine();
				} else if (input.contains("<img id=\"item_image\" src=\""))
					examine = br.readLine();
				else if (input.matches("(?i).+ (price|days):</b> .+")) {
					values[i] = parse(input);
					i++;
				} else if (input.matches("<div id=\"legend\">"))
					break;
			return new GEItem(name, examine, itemID, values);
		} catch (final IOException ignore) {}
		return null;
	}
	/**
	 * Collects data for a given item name from the Grand Exchange website.
	 * @param itemName The name of the item.
	 * @return An instance of GrandExchange.GEItem; <code>null</code> if unable to fetch data.
	 */
	public GEItem lookup(final String itemName) {
		try {
			final URL url = new URL(HOST + "/m=itemdb_rs/results.ws?query=" + itemName + "&price=all&members=");
			final BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
			String input;
			while ((input = br.readLine()) != null)
				if (input.contains("<div id=\"search_results_text\">")) {
					input = br.readLine();
					if (input.contains("Your search for"))
						return null;
				} else if (input.startsWith("<td><img src=")) {
					final Matcher matcher = PATTERN.matcher(input);
					if (matcher.find())
						if (matcher.group(2).contains(itemName))
							return lookup(Integer.parseInt(matcher.group(1)));
				}
		} catch (final IOException ignored) {}
		return null;
	}
	public double parse(String str) {
		if (str != null && !str.isEmpty()) {
			str = stripFormatting(str);
			str = str.substring(str.indexOf(58) + 2, str.length());
			str = str.replace(",", "");
			str = str.trim();
			if (!str.endsWith("%")) {
				if (!str.endsWith("k") && !str.endsWith("m") && !str.endsWith("b"))
					return Double.parseDouble(str);
				return Double.parseDouble(str.substring(0, str.length() - 1))
				        * (str.endsWith("b") ? 1000000000 : str.endsWith("m") ? 1000000 : 1000);
			}
			final int k = str.startsWith("+") ? 1 : -1;
			str = str.substring(1);
			return Double.parseDouble(str.substring(0, str.length() - 1)) * k;
		}
		return -1D;
	}
	public String stripFormatting(final String str) {
		if (str != null && !str.isEmpty())
			return str.replaceAll("(^[^<]+>|<[^>]+>|<[^>]+$)", "");
		return "";
	}

	/**
	 * Provides access to GEItem Information.
	 */
	public class GEItem {
		private final String name;
		private final String examine;
		private final int id;
		private final int guidePrice;
		private final double change30;
		private final double change90;
		private final double change180;
		GEItem(final String name, final String examine, final int id, final double[] values) {
			this.name = name;
			this.examine = examine;
			this.id = id;
			guidePrice = (int) values[0];
			change30 = values[1];
			change90 = values[2];
			change180 = values[3];
		}
		/**
		 * Gets the change in price for the last 30 days of this item.
		 * @return The change in price for the last 30 days of this item.
		 */
		public double getChange30Days() {
			return change30;
		}
		/**
		 * Gets the change in price for the last 90 days of this item.
		 * @return The change in price for the last 90 days of this item.
		 */
		public double getChange90Days() {
			return change90;
		}
		/**
		 * Gets the change in price for the last 180 days of this item.
		 * @return The change in price for the last 180 days of this item.
		 */
		public double getChange180Days() {
			return change180;
		}
		/**
		 * Gets the ID of this item.
		 * @return The ID of this item.
		 */
		public int getID() {
			return id;
		}
		/**
		 * Gets the market price of this item.
		 * @return The market price of this item.
		 */
		public int getGuidePrice() {
			return guidePrice;
		}
		/**
		 * Gets the name of this item.
		 * @return The name of this item.
		 */
		public String getName() {
			return name;
		}
		/**
		 * Gets the description of this item.
		 * @return The description of this item.
		 */
		public String getDescription() {
			return examine;
		}
	}

	private interface BankCollectMethods {
		public int getBankInterface();
		public int getBankLeftCollect();
		public int getBankRightCollect();
		public boolean bankCollectAll();
		public boolean bankIsOpen();
		public boolean bankOpen();
		public boolean bankClose();
	}

	private class BankCollect implements BankCollectMethods {
		private int Interface = 0;
		private int leftCollect = 0;
		private int rightCollect = 0;
		private final int COLLECT_CLOSE = 14;
		public BankCollect(final int slot) {
			SLOT = slot;
			switch (slot) {
				case 1:
					Interface = 19;
					leftCollect = 0;
					rightCollect = 2;
					break;
				case 2:
					Interface = 23;
					leftCollect = 0;
					rightCollect = 2;
					break;
				case 3:
					Interface = 27;
					leftCollect = 0;
					rightCollect = 2;
					break;
				case 4:
					Interface = 32;
					leftCollect = 0;
					rightCollect = 2;
					break;
				case 5:
					Interface = 37;
					leftCollect = 0;
					rightCollect = 2;
					break;
				case 6:
					Interface = 42;
					leftCollect = 0;
					rightCollect = 2;
					break;
			}
		}
		public BankCollect() {}
		public int getBankInterface() {
			return this.Interface;
		}
		public int getBankLeftCollect() {
			return this.leftCollect;
		}
		public int getBankRightCollect() {
			return this.rightCollect;
		}
		public boolean bankIsOpen() {
			return interfaces.get(COLLECT_INTERFACE).isValid();
		}
		public boolean bankOpen() {
			if (!bankIsOpen()) {
				final RSNPC i = npcs.getNearest(BANKERS);
				if (!i.isValid())
					return false;
				mouse.move(i.getPoint());
				mouse.click(false);
				// TODO Interact clicked wrong menu item.
				if (menu.clickIndex(menu.getIndex("Collect"))) {
					if (calc.distanceTo(i) > 1) {
						final long time = System.currentTimeMillis();
						final int max = random(2000, 4000);
						while (System.currentTimeMillis() - time < max) {
							if (players.getMyPlayer().isMoving()) {
								do
									sleep(random(5, 15));
								while (players.getMyPlayer().isMoving() || !i.isOnScreen());
								break;
							}
							sleep(random(5, 15));
						}
					}
					for (int j = 0; j < 10 && !bankIsOpen(); j++)
						sleep(random(100, 200));
					sleep(random(700, 900));
					return bankIsOpen();
				} else
					return false;
			}
			return true;
		}
		public boolean bankCollectAll() {
			if (bankIsOpen()) {
				int boxToCollect;
				boxToCollect = getTotalSlots();
				for (int i = 1; i <= boxToCollect;) {
					final BankCollectMethods k = new BankCollect(i);
					final int inter = k.getBankInterface();
					final int left = k.getBankLeftCollect();
					final int right = k.getBankRightCollect();
					if (interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(left).getActions() != null
					        && interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(left).getActions().length >= 1) {
						interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(left).doClick();
						sleep(random(300, 500));
					}
					if (interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(right).getActions() != null
					        && interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(right).getActions().length >= 1) {
						interfaces.getComponent(COLLECT_INTERFACE, inter).getComponent(right).doClick();
						sleep(random(300, 500));
					}
					i++;
				}
				bankClose();
				return true;
			}
			return false;
		}
		public boolean bankClose() {
			if (bankIsOpen()) {
				if (!interfaces.getComponent(COLLECT_INTERFACE, COLLECT_CLOSE).doClick()) {
					return false;
				}
			}
			sleep(random(700, 900));
			return true;
		}
	}

	private interface GEBuyMethods {
		public int getInterface();
		public int getBuyClick();
		public int getSellClick();
		public boolean isAnOfferCompleted();
	}

	private class GEBuy implements GEBuyMethods {
		private int Interface = 0;
		private int buyClick = 0;
		private int sellClick = 0;
		private final int completeWidth = 124;
		private final int height = 13;
		private final int COMPLETION_BAR_INTERFACE = 13;
		public GEBuy(final int slot) {
			switch (slot) {
				case 1:
					this.Interface = 19;
					this.buyClick = 30;
					this.sellClick = 29;
					break;
				case 2:
					this.Interface = 35;
					this.buyClick = 46;
					this.sellClick = 45;
					break;
				case 3:
					this.Interface = 51;
					this.buyClick = 62;
					this.sellClick = 61;
					break;
				case 4:
					this.Interface = 70;
					this.buyClick = 81;
					this.sellClick = 80;
					break;
				case 5:
					this.Interface = 89;
					this.buyClick = 100;
					this.sellClick = 99;
					break;
				case 6:
					this.Interface = 108;
					this.buyClick = 119;
					this.sellClick = 118;
					break;
			}
		}
		public GEBuy() {}
		public int getInterface() {
			return this.Interface;
		}
		public int getBuyClick() {
			return this.buyClick;
		}
		public int getSellClick() {
			return this.sellClick;
		}
		public boolean isAnOfferCompleted() {
			if (grandExchange.isOpen()) {
				final int boxToCollect = getTotalSlots();
				for (int i = 1; i <= boxToCollect;) {
					final GEBuyMethods k = new GEBuy(i);
					final int inter = k.getInterface();
					if (interfaces.getComponent(GE_INTERFACE, inter).getComponent(COMPLETION_BAR_INTERFACE) != null
					        && interfaces.getComponent(GE_INTERFACE, inter).getComponent(COMPLETION_BAR_INTERFACE).getHeight() == height
					        && interfaces.getComponent(GE_INTERFACE, inter).getComponent(COMPLETION_BAR_INTERFACE).getWidth() == completeWidth)
						return true;
					i++;
				}
			}
			return false;
		}
	}
	private BufferedImage getImage(final String fileName, final String imageURL) {
		try {
			final File dir = new File(Configuration.Paths.getScriptCacheDirectory(), "DynamicWoodcutter");
			if (!dir.exists())
				dir.mkdir();
			final File f = new File(dir + File.separator + fileName);
			if (!f.exists()) {
				BufferedImage image = null;
				final URL url = new URL(imageURL);
				image = ImageIO.read(url);
				ImageIO.write(image, "PNG", f);
				return image;
			}
			final BufferedImage img = ImageIO.read(f);
			return img;
		} catch (final Exception e) {}
		return null;
	}
	private int chooseAntiban() {
		final int num = random(0, 100);
		int k = 35;
		if (num < k)
			return 0;; // Mouse
		if (num >= k && num < k + 55)
			return 1; // Advanced camera
		k += 55;
		if (num >= k && num < k + 2)
			return 2; // Check skill
		k += 2;
		if (num >= k && num < k + 5)
			return 3; // Examine/Misc
		return -1;
	}
	/**
	 * @return True if an Anti-ban was performed, false otherwise.
	 */
	private boolean antiBan() {
		final int j = chooseAntiban();
		if (j == -1)
			return false;
		if (antibanTimer < System.currentTimeMillis()) {
			switch (j) {
				case 0: // Mouse.
					switch (random(0, 5)) {
						case 0:
							antiBan = "Moving mouse off screen.";
							mouse.moveOffScreen();
							sleep(random(1000, 1500));
							break;
						case 1:
							antiBan = "Moving mouse randomly and sleeping.";
							mouse.moveRandomly(200, 400);
							sleep(random(300, 500));
							break;
						case 2:
							antiBan = "Moving mouse randomly.";
							mouse.moveRandomly(random(100, 500));
							break;
						case 3:
							antiBan = "Moving mouse slightly.";
							mouse.moveSlightly();
							sleep(random(300, 500));
							break;
						case 4:
							antiBan = "Moving mouse to random point.";
							mouse.move(random(527, 200), random(744, 464));
							break;
					}
					break;
				case 1:
					antiBan = "Advanced camera movement.";
					new Camera(Camera.ADVANCED);
					break;
				case 2: // Check a skill.
					antiBan = "Checking skill.";
					if (trainFM && random(0, 2) == 0)
						skills.doHover(Skills.INTERFACE_FIREMAKING);
					else
						skills.doHover(Skills.INTERFACE_WOODCUTTING);
					sleep(random(500, 2500));
					break;
				case 3: // Examine
					outer: switch (random(0, 6)) {
						case 0:
							final RSItem tinderbox = inventory.getItem(tinderboxID);
							if (trainFM && tinderbox != null) {
								antiBan = "Moving Tinderbox.";
								final int idx = tinderbox.getComponent().getComponentIndex();
								RSItem dest = inventory.getItemAt(random(8, 20));
								while (dest.getComponent().getIndex() == idx)
									dest = inventory.getItemAt(random(8, 20));
								tinderbox.getComponent().doHover();
								RSComponent component = dest.getComponent();
								mouse.drag(
								    random(component.getAbsoluteX(), component.getAbsoluteX() + component.getWidth()),
								    random(component.getAbsoluteY(), component.getAbsoluteY() + component.getHeight()));
							} else
								break outer;
						case 1:
							antiBan = "Right clicking a player.";
							RSPlayer target = null;
							for (final RSPlayer p : players.getAll())
								if (p.isOnScreen() && !players.getMyPlayer().equals(p)) {
									target = p;
									break;
								}
							if (target != null) {
								final Point p = target.getScreenLocation();
								mouse.click(p, false);
								sleep(300, 1000);
								mouse.moveSlightly();
							}
							break;
						case 2:
							antiBan = "Right clicking an object.";
							RSObject objTarg = null;
							for (final RSObject p : objects.getAll())
								if (p.isOnScreen()) {
									objTarg = p;
									break;
								}
							if (objTarg != null) {
								objTarg.doClick(false);
								if (random(0, 5) == 0) {
									sleep(random(500, 1000));
									menu.click("Examine");
									sleep(random(100, 300));
								}
								mouse.moveSlightly();
							}
							break;
						case 3:
							antiBan = "Right clicking an item.";
							RSItem itemTarg = null;
							for (final RSItem p : inventory.getItems())
								if (p != null) {
									itemTarg = p;
									break;
								}
							if (itemTarg != null) {
								itemTarg.doClick(false);
								sleep(300, 700);
								if (random(0, 5) == 0) {
									menu.click("Examine");
									sleep(random(100, 300));
								}
								mouse.moveSlightly();
							}
							break;
						case 4:
							antiBan = "Checking XP total.";
							if (interfaces.getComponent(548, 0).interact("Toggle XP Total")) {
								sleep(700, 2200);
								interfaces.getComponent(548, 0).interact("Toggle XP Total");
								sleep(random(100, 400));
								mouse.moveSlightly();
							}
							break;
						case 5:
							antiBan = "Opening random tab.";
							if (random(0, 2) == 0)
								game.openTab(Tab.values()[random(0, 16)]);
							else
								game.openTab(Tab.values()[random(0, 16)], true);
							sleep(random(500, 1500));
							break;
					}
					break;
			}
			if (powerchop && !trainFM)
				antibanTimer = System.currentTimeMillis() + random(15000, 60000);
			else
				antibanTimer = System.currentTimeMillis() + random(8000, 25000);
		} else
			return false;
		if (!antiBan.contains("Moving camera randomly.") && !antiBan.contains("Advanced camera movement."))
			antiBan = "";
		return true;
	}
	private void advancedCameraMovement(int timeOutMin, int timeOutMax) {
		final int random1 = random(timeOutMin / 4, timeOutMax / 4);
		final int random2 = random(timeOutMin / 4, timeOutMax / 4);
		final int random3 = random(timeOutMin / 2, timeOutMax / 2);
		if (random(0, 2) == 0)
			keyboard.pressKey((char) KeyEvent.VK_RIGHT);
		else
			keyboard.pressKey((char) KeyEvent.VK_LEFT);
		sleep(random3);
		if (random(0, 4) == 0)
			keyboard.pressKey((char) KeyEvent.VK_DOWN);
		else
			keyboard.pressKey((char) KeyEvent.VK_UP);
		if (random(0, 2) == 0) {
			sleep(random1);
			keyboard.releaseKey((char) KeyEvent.VK_RIGHT);
			keyboard.releaseKey((char) KeyEvent.VK_LEFT);
			sleep(random2);
			keyboard.releaseKey((char) KeyEvent.VK_UP);
			keyboard.releaseKey((char) KeyEvent.VK_DOWN);
		} else {
			sleep(random1);
			keyboard.releaseKey((char) KeyEvent.VK_UP);
			keyboard.releaseKey((char) KeyEvent.VK_DOWN);
			sleep(random2);
			keyboard.releaseKey((char) KeyEvent.VK_RIGHT);
			keyboard.releaseKey((char) KeyEvent.VK_LEFT);
		}
	}
	private State getState() {
		if (interfaces.get(244).containsText("may I ask you to speak") || needTutoring) {
			status = "GE Tutor";
			needTutoring = true;
			return State.GETUTOR;
		}
		if (checkBank) {
			status = "Banking";
			return State.CHECKBANK;
		}
		if (useDeposit) {
			status = "Depositing";
			return State.DEPOSIT;
		}
		if (!inventory.contains(runeHatchetID)
		        && (runeHatchetPrice * 1.2 < totalCash && runeHatchetPrice != -1 && wcLvl() >= 41
		                && !useAvailableHatchets || sentOffer)) {
			status = "Rune hatchet";
			return State.BUYRUNEHATCHET;
		}
		if (skills.getCurrentExp(Skills.WOODCUTTING) < 500 && !sawLamp || inventory.contains(13439)
		        || npcs.getNearest(7942) != null && calc.distanceTo(npcs.getNearest(7942)) < 10) {
			status = "Lamps";
			return State.LAMPS;
		}
		if (trainFM && !inventory.contains(tinderboxID)) {
			status = "Tinderbox";
			return State.BUYTINDERBOX;
		}
		if (!useAvailableHatchets) {
			bestHatchetAvailable = bestHatchetAvailable();
			if (bestHatchetAvailable == -1) {
				status = "Buying hatchet";
				return State.BUYHATCHET;
			}
			if (inventory.contains(bronzeHatchetID) && !inventory.contains(steelHatchetID) && inventory.contains(995)
			        && inventory.getItem(995).getStackSize() >= 200) {
				status = "Buying hatchet";
				return State.BUYHATCHET;
			}
			if (bestHatchetAvailable == bronzeHatchetID) {
				status = "Trees";
				return State.TREE;
			}
			if (!inventory.contains(bestHatchetAvailable)) {
				status = "Upgrading Hatchet";
				return State.CHECKBANK;
			}
		}
		if (burnLogs) {
			status = "Burning logs";
			return State.BURN;
		}
		if (useAvailableHatchets) {
			if (!checkedBank) {
				status = "Hatchet count";
				return State.CHECKBANK;
			}
			bestHatchetAvailable = bestHatchetAvailable();
			final RSItem r = inventory.getItem(bestHatchetAvailable);
			if (bestHatchetAvailable != -1 && r == null) {
				status = "Upgrading Hatchet";
				return State.CHECKBANK;
			}
			if (checkedBank && bestHatchetAvailable == -1) {
				log("No hatchet available in your bank or inventory for your level.");
				end = true;
			}
		}
		if (wcLvl() >= 60 && (yewAfter60 || locked && yewSelected)) {
			status = "Yews";
			useBank = true;
			return State.YEW;
		}
		if (trainFM) {
			if (fmLvl() >= 30)
				if (wcLvl() >= 30 && (!locked || locked && willowSelected)) {
					status = "Burning Willows";
					if (willowLocation == 1)
						return State.WILLOWRIMM;
					if (willowLocation == 2)
						return State.WILLOWLUMB;
					if (willowLocation == 3)
						return State.WILLOWPORT;
					if (willowLocation == 4)
						return State.WILLOWDRAY;
				}
			if (fmLvl() >= 15) {
				if (wcLvl() < 15 && (!locked || locked && treeSelected)) {
					status = "Burn Trees";
					return State.TREE;
				}
				if (wcLvl() >= 15 && (!locked || locked && oakSelected)) {
					status = "Burn Oaks";
					return State.OAK;
				}
			}
			if (fmLvl() < 15 && (!locked || locked && treeSelected)) {
				status = "Burn Trees";
				return State.TREE;
			}
			status = "Option unavailable" + manageDots();
			return State.error;
		}
		if (wcLvl() >= 30 && (!locked || locked && willowSelected)) {
			status = "Willows";
			switch (willowLocation) {
				case 1:
					if (bankWillow)
						useBank = true;
					else
						useBank = false;
					if (dropWillow)
						drop = true;
					else
						drop = false;
					if (powerchopWillow)
						powerchop = true;
					else
						powerchop = false;
					return State.WILLOWRIMM;
				case 2:
					if (bankWillow)
						useBank = true;
					else
						useBank = false;
					if (dropWillow)
						drop = true;
					else
						drop = false;
					if (powerchopWillow)
						powerchop = true;
					else
						powerchop = false;
					return State.WILLOWLUMB;
				case 3:
					if (bankWillow)
						useBank = true;
					else
						useBank = false;
					if (dropWillow)
						drop = true;
					else
						drop = false;
					if (powerchopWillow)
						powerchop = true;
					else
						powerchop = false;
					return State.WILLOWPORT;
				case 4:
					if (bankWillow)
						useBank = true;
					else
						useBank = false;
					if (dropWillow)
						drop = true;
					else
						drop = false;
					if (powerchopWillow)
						powerchop = true;
					else
						powerchop = false;
					return State.WILLOWDRAY;
			}
		}
		if (wcLvl() >= 15 && (!locked && wcLvl() < 30 || locked && oakSelected)) {
			status = "Oaks";
			if (bankOak)
				useBank = true;
			else
				useBank = false;
			if (dropOak)
				drop = true;
			else
				drop = false;
			if (powerchopOak)
				powerchop = true;
			else
				powerchop = false;
			return State.OAK;
		}
		if (wcLvl() < 15 && (!locked || locked && treeSelected)) {
			status = "Trees";
			if (dropTree)
				drop = true;
			else
				drop = false;
			if (bankTree)
				useBank = true;
			else
				useBank = false;
			if (!dropTree && !bankTree) {
				drop = false;
				useBank = false;
			}
			return State.TREE;
		}
		status = "Option unavailable" + manageDots();
		return State.error;
	}
}