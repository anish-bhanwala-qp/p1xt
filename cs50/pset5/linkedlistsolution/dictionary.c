// Implements a dictionary's functionality
#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>

#include "dictionary.h"
#include "linkedlist.h"

//Initialize linked list
linkedlist *ll;

// Returns true if word is in dictionary else false
bool check(const char *word)
{
    return findword(ll, word);
}

// Loads dictionary into memory, returning true if successful else false
bool load(const char *dictionary)
{
    ll = malloc(sizeof(linkedlist));

    FILE *fileptr = fopen(dictionary, "r");
    if (!fileptr)
    {
        return false;
    }

    ll->first = NULL;

    char c;
    char word[LENGTH + 1];
    int i = 0;

    while ((c = getc(fileptr)) != EOF) {

        //Reset index if its's line end
        if (c == '\n')
        {
            //Terminate string
            word[i++] = '\0';
            i = 0;
            //add word to linked list
            addnode(ll, word);
        }
        else
        {
            word[i++] = c;
        }

    }

    printall(ll);

    return true;
}

// Returns number of words in dictionary if loaded else 0 if not yet loaded
unsigned int size(void)
{
    return sizeoflinkedlist(ll);
}

// Unloads dictionary from memory, returning true if successful else false
bool unload(void)
{
    return freememory(ll);
}
