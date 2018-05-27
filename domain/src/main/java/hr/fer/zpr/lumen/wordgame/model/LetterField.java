package hr.fer.zpr.lumen.wordgame.model;

public class LetterField {

    private Letter[] letters;

    public LetterField(int length){
        letters=new Letter[length];
    }


    public boolean isWordCorrect(Word word){
        StringBuilder result=new StringBuilder();
        for(Letter letter : letters){
            if(letter==null) return false;
            result.append(letter.value);
        }
        if(result.toString().equals(word.stringValue)) return true;
        return false;
    }

    public void insertLetterIntoField(Letter letter, int position){
            letters[position]=letter;
    }

    public void removeLetterFromField(int n){
        letters[n]=null;
        return;
    }

    public boolean isLetterAtNCorrect(int n,Word word){
        if(letters[n]!=null && letters[n].value.equals(word.letters.get(n).value)) return true;
        return false;
    }

    public boolean isFull(){
        for(int i=0;i<letters.length;i++){
            if(letters[i]==null) return false;
        }
        return true;
    }


}
