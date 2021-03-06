package kiuya.english.game;

public class StaticVariable {
	public static String realPath = ""; //檔案實際執行位置
	public static String msg = ""; //記錄遊戲訊息
	public static String word = ""; //使用者輸入字串
	public static int leader = 1; //目前操控者
	public static int peopleNum = 1; //遊戲人數
	public static String hisWord[] = null; //記錄每一位操作者的當前單字
	public static boolean isExit = false; //是否離開遊戲
	public static int playersSurrender[] = null; //記錄投降者(值=0表示尚未認輸，值為1表示已認輸)
	public static String playerComplementWord = "";
	public static GameModel game = null; //遊戲模式
	public static String wordArr[][][]; //所有單字資訊
	public static int maxUsers, maxHelp, maxComplementWord; //maxUsers=最多參與遊戲人數，maxHelp=最大救援次數，maxComplementWord=最大印象單字補充次數
	public static boolean historyWord, complementWord; //historyWord=記錄歷史單字，complementWord=單字補充
}
