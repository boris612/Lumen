package wordgame.db.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import hr.fer.zpr.lumen.wordgame.model.Category;
import hr.fer.zpr.lumen.wordgame.model.Image;
import hr.fer.zpr.lumen.wordgame.model.Language;
import wordgame.db.model.DbCategory;
import wordgame.db.model.DbImage;

public class DataDomainMapper {


    public static Image DatabaseImageToDomain(DbImage image) {
        return new Image(image.path);
    }

    public static Collection<Category> DatabaseCategoriesToDomain(List<DbCategory> categories) {
        List<Category> result = new ArrayList<>();
        for (DbCategory category : categories) {
            result.add(Category.valueOf(category.value.toUpperCase()));
        }
        return result;
    }

    public static Collection<DbCategory> DomainCategoriesToDatabase(Collection<Category> categories) {
        List<DbCategory> result = new ArrayList<>();
        for (Category category : categories) {
            result.add(new DbCategory(category.name()));
        }
        return result;

    }

    public static Set<Category> categoriesFromStrings(Set<String> categories) {
        Set<Category> result = new HashSet<>();
        for (String category : categories) {
            result.add(Category.valueOf(category.toUpperCase()));
        }
        return result;
    }

    public static Language languageFromString(String language) {
        return Language.valueOf(language.toUpperCase());
    }


}
