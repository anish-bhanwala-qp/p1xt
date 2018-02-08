#include <stdio.h>

typedef struct linkedlistnode
{
    char *word;
    struct linkedlistnode *next;
} node;

typedef struct ll
{
    node *first;
} linkedlist;


node *getlastnode(linkedlist *ll);
void addnode(linkedlist *ll, char *word);
bool freememory(linkedlist *ll);
bool findword(linkedlist *ll, const char *word);
void printall(linkedlist *ll);
void tolowercase(char *word);
int sizeoflinkedlist(linkedlist *ll);
