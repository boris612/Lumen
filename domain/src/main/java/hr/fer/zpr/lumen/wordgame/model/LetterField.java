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
        if(letters[position]==null){
            letters[position]=letter;
        }
    }

    public void removeLetterFromField(Letter letter){
        for(int i=0;i<letters.length;i++){
            if(letters[i].equals(letter)){
                letters[i]=null;
                return;
            }
        }
    }


}
