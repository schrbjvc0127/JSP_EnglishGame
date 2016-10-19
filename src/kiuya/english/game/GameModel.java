package kiuya.english.game;
import java.util.Scanner;

public abstract class GameModel{
	Setting set;
	Scanner keyboard = new Scanner(System.in);
	
	String u_word,HisWord;	//u_word記錄玩家輸入單字，HisWord紀錄前一個單字
	String msg;	//將訊息傳至前端
	boolean hasExit,hasRecord; //指令確認
	boolean firstInput; //是否第一次輸入
	boolean hasCmd;	//判斷字母是否存在
	boolean findword, findSimilar; //findword是否找到當前單字，findSimilar是否找到和印象單字match的單字

    GameModel() {
    	set = new Setting();
    }
    
    public void iniGame(){ //初始化資料
    	firstInput=true; //第一次輸入單字
    	HisWord=""; //清空歷史單字
    	hasCmd = false;	//判斷字母是否存在
    	hasExit=false;
    	hasRecord=false;
    	
    	try{
    		set.setConfig(); //呼叫Setting的Method匯入外部設定檔
    		set.setWordArray(); //呼叫Setting的Method設定陣列大小
    		set.inputAlph(); //呼叫Setting的Method設定儲存單字至陣列中
    	}catch(Exception e){
    		System.out.println("設定出現問題iniGame");
    	}
    	for(int i=0; i<=25; i++){
    		for(int j=0; j<StaticVariable.wordArr[i].length; j++){
    			StaticVariable.wordArr[i][j][2]="n";
    			StaticVariable.wordArr[i][j][3]="n";
    		}
		}
    }
    
    public void runGame(){
    	try{
    		StartGame();
    	}catch(Exception e){
    		msg = "執行出現問題runGame";
    	}
    }
    
    public void saveRecord(){
    	set.SaveWordToFile(hasRecord);
    }
    //******************************************************//
    
    //*******************兩個子類別的共同抽象方法*******************//
    abstract void StartGame(); //開始遊戲
    abstract boolean CheckCmd(String word); //檢查輸入的字串是否為指令
    abstract void CheckFindWord(int alphword, int alphindex, boolean errMsg); //檢查是否找到單字
    //******************************************************//

    
    //********************兩個子類別的共同方法********************//
    void userPlay(){ //玩家開始行動
   		u_word = StaticVariable.word;
   		hasCmd = CheckCmd(u_word);	//判斷字母是否為指令
   		if(!hasCmd){
   			if(firstInput){
   				CheckWord(u_word);
   				if(findword && firstInput)
					firstInput=false;
   			}else{
   				if(CheckHead(u_word))
   					CheckWord(u_word);
   			}
   		}
	}
    
    boolean CheckHead(String s1){ //檢查字母開頭是否與上一個單字字尾相同
		if(!(s1.equals("noinput"))){
			u_word = ToLowerCase(u_word); //將字母全都轉成小寫比對
			if((GetFirstChar(u_word)).equals(GetLastChar(HisWord))){ //看是否目前單字字首和上一個單字字尾是否相同
				return true;
			}else{
				StaticVariable.msg = "您輸入的單字開頭有誤!!";
				return false;
			}
		}
		return true; //表示尚未輸入任何一個單字，所以不用檢查字首
	}
    
    void CheckWord(String s1){ //檢查及印出目前單字
    	int alphword=0,alphindex=0;	//alphword儲存目前字母的單字索引，alphindex儲存目前字母索引
   		boolean errMsg = false; //若值為false表示查無此單字，若為true表示此單字重複使用
   		String headword;
   		
   		if(firstInput && u_word.equals("\\c")){
   			alphindex = (int)(Math.random()*StaticVariable.wordArr.length);
   		}else{
   			if(s1.equals("noinput"))
   				headword = GetLastChar(HisWord);	//取得上一個單字的字尾，當作這次的字首
   			else
   				headword = GetFirstChar(u_word);	//取得輸入單字的字首，當作這次的字首
   		
   			for(int i=0; i<StaticVariable.wordArr.length; i++){	//執行26個字母
   				if(StaticVariable.wordArr[i][0][0].substring(0,1).toLowerCase().equals(headword)){	//判斷目前單字字首為:a~z其中一個
   					alphindex = i;	//紀錄字首的字母索引
   					break;
   				}
   			}
   		}
   		
   		if(s1.equals("noinput")){
   			int index = (int)(Math.random()*StaticVariable.wordArr[alphindex].length);	//隨機產生單字的索引
			while(!(StaticVariable.wordArr[alphindex][index][2].equals("n"))){	//重複執行直到單字沒使用過
				index = (int)(Math.random()*StaticVariable.wordArr[alphindex].length);	//隨機產生單字的索引
			}
			alphword = index;	//將索引存入
			u_word = StaticVariable.wordArr[alphindex][alphword][0];	//將找到單字存入
			findword = true;	//找到單字
   		}else{
   			for(int i=0; i<StaticVariable.wordArr[alphindex].length; i++){
   				if(StaticVariable.wordArr[alphindex][i][0].toLowerCase().equals(s1)){
   					if(StaticVariable.wordArr[alphindex][i][2].equals("n")){
   						alphword = i;
   	   					findword = true;
   	   					break;
   					}else{
   						errMsg = true;
   						StaticVariable.msg = "此單字已被使用過!!";
   					}
   				}
   			}	
   		}
   		this.CheckFindWord(alphword, alphindex, errMsg);
	}
    
    void CheckComplementWord(){
    	findSimilar = false;
    	String word = keyboard.next();
    	if(word.length() >= 5){
    		int alphindex=104;
    		String headword = GetFirstChar(word);
    		for(int i=0; i<StaticVariable.wordArr.length; i++){	//執行26個字母
    			if(StaticVariable.wordArr[i][0][0].substring(0,1).toLowerCase().equals(headword)){	//判斷目前單字字首為:a~z其中一個
    				alphindex = i;	//紀錄字首的字母索引
    				break;
    			}
    		}
    		if(alphindex != 104){
    			StaticVariable.msg = "搜尋結果如下:\n";
    			for(int i=0; i<StaticVariable.wordArr[alphindex].length; i++){
    				CompareWord(word, alphindex, i);
    			}
    			if(!findSimilar)
    				StaticVariable.msg += "很抱歉沒有類似的單字!!";
    		}else{
    			StaticVariable.msg = "輸入單字的字首有問題!!";
    		}
    	}else{
    		StaticVariable.msg = "你輸入的印象單字少於5個字!!";
    	}
    }
    
    void CompareWord(String word, int x, int y){
    	int count = 0; //記錄match到的字元有幾個
    	String wordTmp = word.substring(1); //最後比對match次數用的
    	word = word.substring(1); //先去掉字首，因為已經確定字首不需要比
    	String word2 = StaticVariable.wordArr[x][y][0].substring(1); //要被比對的單字，並且去掉字首
    	if((word2.length() == word.length()) || (word2.length() == word.length()+1) || (word2.length() == word.length()-1)){
    		char charArr[] = word2.toCharArray();
    		while(word.length() > 0){
    			for(int i=0; i<charArr.length; i++){
        			if(word.substring(0,1).equals(String.valueOf(charArr[i]))){
        				charArr[i] = ' ';
        				count++;
        				break;
        			}
        		}
    			word = word.substring(1);
    		}
    		if((count == wordTmp.length()) || (count == wordTmp.length()-1)){
    			StaticVariable.msg += StaticVariable.wordArr[x][y][0];
    			findSimilar = true;
    		}
    	}
    }
    
    String ToLowerCase(String s1){
		return s1.toLowerCase();	//將傳入的字轉小寫
	}
    
	String GetFirstChar(String s1){
		return s1.substring(0,1);	//取得傳入字的字首
	}
	
    String GetLastChar(String s1){
    	return s1.substring(s1.length()-1);	//取得目前單字的字尾
    }
    //******************************************************//
    
    
    //********************UserVsCom專有方法********************//
    //                                                      //
    //******************************************************//
    
    
    //********************UserVsUser專有方法*******************//
    //int CheckPeople(); 檢查遊戲人數是否符合範圍                                                          //
    //void setUser(); 設定每個玩家的基本屬性(例:救援次數、是否投降等)      //
    //void CheckWinner(); 檢查是否有玩家獲勝                                                                  //
    //void nextUser(); 尋找下一位輸入單字的玩家                                                               //
    //******************************************************//
}