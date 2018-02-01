// Helper functions for music

#include <stdio.h>
#include <cs50.h>
#include <string.h>
#include <math.h>

#include "helpers.h"

int char_to_float(char c);
int char_to_int(char c);
int get_octave(string note);
int dist_within_A4(int octave);
int num_of_oct_in_between(int octave);
int dist_within_octave(char letter, bool is_accidental, int octave);
int letter_pos_in_oct(char letter);
bool has_accidental(string note);
char get_note_letter(string note);
char get_previous_letter(char letter);

const char ALL_NOTES[] = {'C', '#', 'D', '#', 'E', 'F',
                          '#', 'G', '#', 'A', '#', 'B'
                         };

int char_to_int(char c)
{
    return c - '0';
}

int char_to_float(char c)
{
    return (float)(c - '0');
}

// Converts a fraction formatted as X/Y to eighths
int duration(string fraction)
{
    //convert the two numbers to float
    float num = char_to_float(fraction[0]);
    float den = char_to_float(fraction[2]);

    //Divide and get the fraction number, divide by 1/8 or multiply by 8
    float result = ((float)num / (float)den) * 8.0f;

    //printf ("fraction: %s, num: %f, den: %f, dura: %f\n", fraction, num, den, result);
    return round(result);
}

// Calculates frequency (in Hz) of a note
int frequency(string note)
{
    char letter = get_note_letter(note);
    int octave = get_octave(note);

    int dist_from_A4 = dist_within_A4(octave) + num_of_oct_in_between(octave) * 12
                       + dist_within_octave(letter, has_accidental(note), octave);

    if (octave < 4)
    {
        dist_from_A4 = -1 * dist_from_A4;
    }

    // printf("dist_within_A4: %i , num_of_oct_in_between: %i , dist_within_octave: %i,  dist_from_A4: %i \n",
    //     dist_within_A4(octave),
    //     num_of_oct_in_between(octave),
    //     dist_within_octave(letter, has_accidental(note), octave),
    //     dist_from_A4);

    //2n/12 × 440, n is number of semitones between

    int result = round(pow(2, (float)dist_from_A4 / 12.0f) * 440);

    //printf("Note: %s, Freq: %i\n", note, result);

    return round(result);
}

int dist_within_A4(int octave)
{
    if (octave == 4)
    {
        return 0;
    }
    else if (octave < 4)
    {
        return 10;
    }
    else
    {
        return 2;
    }
}

int num_of_oct_in_between(int octave)
{
    // E.g. octave between A4 and A3 will be 3 + 1 - 4 = 0
    if (octave == 4)
    {
        return 0;
    }
    else if (octave < 4)
    {
        return 4 - octave - 1;
    }
    else
    {
        return octave - 4 - 1;
    }
}

int dist_within_octave(char letter, bool is_accidental, int octave)
{
    /*
       Notes order within octave is CDEFGAB
       ASCII code of letter A is 65 and letter B is 66
    */

    int letter_pos = letter_pos_in_oct(letter);
    if (is_accidental)
    {
        letter_pos++;
    }

    if (octave == 4)
    {
        return -1 * (9 - letter_pos);
    }
    else if (octave < 4)
    {
        // 12 - 3 -1 for A3
        return sizeof(ALL_NOTES) - letter_pos - 1;
    }
    else
    {
        return letter_pos + 1;
    }
}

int letter_pos_in_oct(char letter)
{
    for (int i = 0; i < sizeof(ALL_NOTES); i++)
    {
        if (ALL_NOTES[i] == letter)
        {
            return i;
        }
    }

    return -1;
}

bool has_accidental(string note)
{
    return strlen(note) == 3;
}

char get_note_letter(string note)
{
    char letter = note[0];

    //If accidental is flat, convert it to sharp
    if (has_accidental(note) && note[1] == 'b')
    {
        return get_previous_letter(letter);
    }
    else
    {
        return letter;
    }
}

char get_previous_letter(char letter)
{
    for (int i = 0; i < sizeof(ALL_NOTES); i++)
    {
        if (ALL_NOTES[i] == letter)
        {
            return ALL_NOTES[i - 2];
        }
    }

    return -1;
}

int get_octave(string note)
{
    if (has_accidental(note))
    {
        return char_to_int(note[2]);
    }
    else
    {
        return char_to_int(note[1]);
    }
}

// Determines whether a string represents a rest
bool is_rest(string s)
{
    if (s == NULL)
    {
        //printf("is_rest, s: %s, true Null\n", s);
        return true;
    }
    else if (strcmp(s, "") == 0 || strcmp(s, "\n") == 0)
    {
        //printf("is_rest, s: %s,  true, empty or new line \n", s);
        return true;
    }

    //printf("is_rest: false\n");
    return false;
}
