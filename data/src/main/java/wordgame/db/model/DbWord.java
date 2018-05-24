package wordgame.db.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Relation;

import java.util.Set;

@Entity(tableName = "words")
public class DbWord {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String word;

    @ForeignKey(entity=DbLanguage.class,parentColumns = "id",childColumns = "languageId")
    public int languageId;

    public DbWord(String word, int languageId){
        this.word=word;
        this.languageId=languageId;
    }


    @Override
    public boolean equals(Object obj) {
        if( (obj instanceof DbWord)==false){
            return false;
        }
        DbWord second=(DbWord)obj;
        if(!this.word.equals(second.word)){
            return false;
        }
        if(! (this.languageId==second.languageId)){
            return false;
        }
        return true;
    }
}
