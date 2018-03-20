package ca.csf.mobile1.utils.view;

import android.text.InputFilter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * Filtre de caractères. Permet d'indiquer à un "EditText" d'accepter seulement un certain lot de caractères.
 * <p>
 * Par défaut, accepte uniquement les caractères suivants :
 * <p>
 * a b c d e f g h i j k l m n o p q r s t u v w x y z A B C D E F G H I J K L M N O P Q R S T U V W X Y Z (espace) (point)
 */
public class CharactersFilter implements InputFilter {

    /**
     * Caractères acceptés par défaut.
     */
    public static final char[] DEFAULT_ACCEPTED_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ .".toCharArray();

    private final char[] acceptedCharacters;

    /**
     * Construit un nouveau CharatersFilter acceptant uniquement les caractères défini dans {@link CharactersFilter#DEFAULT_ACCEPTED_CHARACTERS DEFAULT_ACCEPTED_CHARACTERS}.
     */
    public CharactersFilter() {
        this(DEFAULT_ACCEPTED_CHARACTERS);
    }

    /**
     * Construit un nouveau CharatersFilter acceptant uniquement les caractères reçus à la construction.
     *
     * @param acceptedCharacters Tableau des caractères à accepter. Tous les autres seront refusés.
     */
    public CharactersFilter(char[] acceptedCharacters) {
        this.acceptedCharacters = acceptedCharacters;
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int destinationStart, int destinationEnd) {
        //Source code taken from class android.text.method.NumberKeyListener. Added little tweaks.
        int i;
        for (i = start; i < end; i++) {
            if (isNotAccepted(source.charAt(i))) {
                break;
            }
        }

        if (i == end) {
            return null;
        }

        if (end - start == 1) {
            return "";
        }

        SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
        i -= start;
        end -= start;

        for (int j = end - 1; j >= i; j--) {
            if (isNotAccepted(source.charAt(j))) {
                filtered.delete(j, j + 1);
            }
        }

        return filtered;
    }

    private boolean isNotAccepted(char charToVerify) {
        for (char currentChar : acceptedCharacters) {
            if (currentChar == charToVerify) {
                return false;
            }
        }
        return true;
    }

}
