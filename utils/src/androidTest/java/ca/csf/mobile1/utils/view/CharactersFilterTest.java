package ca.csf.mobile1.utils.view;

import android.text.SpannableString;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CharactersFilterTest {

    @Test
    public void filterDefaultAcceptsBasicCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter();

        String source = new String(CharactersFilter.DEFAULT_ACCEPTED_CHARACTERS);
        SpannableString dest = new SpannableString("");
        assertNull(charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                    dest, 0, dest.length()));
    }

    @Test
    public void filterDefaultRefuseNonBasicCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter();

        String source = "éèàÀÉÈ0123456789!#$%?{}[]@";
        SpannableString dest = new SpannableString("");
        assertEquals("", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                          dest, 0, dest.length()).toString());
    }

    @Test
    public void filterDefaultRemovesNonBasicCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "ABCD!#$%?{}[]@EFGH";
        SpannableString dest = new SpannableString("");
        assertEquals("ABCDEFGH", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                                  dest, 0, dest.length()).toString());
    }

    @Test
    public void filterAcceptsSentCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SpannableString dest = new SpannableString("");
        assertNull(charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                    dest, 0, dest.length()));
    }

    @Test
    public void filterRefuseNonSentCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "éèàÀÉÈ0123456789abcdefghijklmnopqrstuvwxyz!#$%?{}[]@";
        SpannableString dest = new SpannableString("");
        assertEquals("", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                          dest, 0, dest.length()).toString());
    }

    @Test
    public void filterRemovesUnwantedCharacters() {
        CharactersFilter charactersOnlyInputFilter = new CharactersFilter("ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray());

        String source = "ABCD!#$%?{}[]@EFGH";
        SpannableString dest = new SpannableString("");
        assertEquals("ABCDEFGH", charactersOnlyInputFilter.filter(source, 0, source.length(),
                                                                  dest, 0, dest.length()).toString());
    }

}