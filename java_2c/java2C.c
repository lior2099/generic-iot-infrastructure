
/**********************************
File Name : java2C.c
Author: lior shalom
Reviewer : ido
Date : 28.07.24
************************************/

#include <stdlib.h> /*malloc*/
#include <stdio.h> /*printf*/
#include <string.h> /*strlen*/

#define BUFFER_SIZE (100)
enum {UNLOAD , LOAD}; /*big*/

typedef struct class class_t;
typedef void *(*fp_vtable_t)(void *);


/************************ Object typedef*******************************/
typedef struct object object_t;
typedef int(*equals_t)(object_t *this, object_t *other);
typedef int(*hashCode_t)(object_t *this);
typedef class_t(*getClass_t)(object_t *this);
typedef char*(*tostring_t)(object_t *this);
typedef void(*finalize_t)(object_t *this);

enum {EQUALS , HASH, CLASS,  TOSTRING , FINALIZE ,OBJ_METHODS};

 
/************************ MutableInteger typedef *******************************/
typedef struct mutable_integer mi_t;
typedef void(*set_I_t)(void *this, int data);
typedef int(*get_I_t)(void *this);

enum {MI_EQUALS , MI_HASH, MI_SET , MI_GET , MI_METHODS};

/************************ Animal typedef *******************************/
typedef struct animal animal_t;
typedef void(*animal_hello_t)(animal_t* this);
typedef int(*animal_masters_t)(animal_t* this);
typedef char*(*animal_tostring_t)(animal_t* this);
typedef void(*animal_finalize_t)(animal_t* this);

enum {HELLO = OBJ_METHODS , MASTERS  , ANIMAL_METHODS};

/************************ Dog typedef *******************************/
typedef struct dog dog_t;
typedef void(*dog_hello_t)(dog_t* this);
typedef char*(*dog_tostring_t)(dog_t* this);
typedef void(*dog_finalize_t)(dog_t* this);

/************************ Cat typedef *******************************/
typedef struct cat cat_t;
typedef char*(*cat_tostring_t)(cat_t* this);
typedef void(*cat_finalize_t)(cat_t* this);

/************************ LegendaryAnimal typedef *******************************/
typedef struct LegendaryAnimal la_t;
typedef void(*la_hello_t)(la_t* this);
typedef char*(*cat_tostring_t)(cat_t* this);
typedef void(*cat_finalize_t)(cat_t* this);


/************************ class *******************************/
struct class
{
    char *class_name;
    class_t *super_class;
    size_t size;
    fp_vtable_t *vtable; 
};

struct object
{
    class_t *metadata;
};

struct mutable_integer
{
    object_t obj;
    int value;
};

struct animal
{
    object_t obj;
    int num_legs;
    int num_masters;
    int ID;
};

struct dog
{
    animal_t super;
    int num_legs;
};

struct cat
{
    animal_t super;
    char *colors;
    int num_masters;
};

struct LegendaryAnimal
{
    cat_t super;
};




/************************ Object Init *******************************/

int Object_Equals(object_t *this, object_t *other);
int Object_HashCode(object_t *this);
class_t *Object_GetClass(object_t *this);
char *Object_ToString(object_t *this);
object_t *Object();
object_t *Object_Creat(class_t *class);
void Object_Constructor(object_t *obj);
void Object_Finalize(object_t *obj);
void ObjectTest();

fp_vtable_t obj_vtable[OBJ_METHODS] = { 
    (fp_vtable_t)Object_Equals,
    (fp_vtable_t)Object_HashCode,
    (fp_vtable_t)Object_GetClass,
    (fp_vtable_t)Object_ToString,
    (fp_vtable_t)Object_Finalize
};
class_t object_metadata = {"Object", NULL, sizeof(object_t), obj_vtable};


/************************ MutableInteger Init *******************************/

int MI_Equals(object_t *this, object_t *other);
int MI_HashCode(object_t *this);
mi_t *MutableInteger();
mi_t *MI_Creat(class_t *class);
void MI_Constructor(mi_t *obj , int data);
void MI_Set_I(void *this, int data);
int MI_Get_I(void *this);
void MITest();

fp_vtable_t mi_vtable[MI_METHODS] = { 
    (fp_vtable_t)MI_Equals,
    (fp_vtable_t)MI_HashCode,
    (fp_vtable_t)MI_Set_I,
    (fp_vtable_t)MI_Get_I,
};
class_t mi_metadata = {"MutableInteger", &object_metadata, sizeof(mi_t), mi_vtable};


/************************ Animal Init *******************************/

int Animal_counter = 0;
int flag_load = UNLOAD; 

animal_t *Animal();
animal_t *Animal_I(int masters);
animal_t *Animal_Creat(class_t *class);
void Animal_Constructor(animal_t *animal);
void Animal_Constructor_I(animal_t *animal , int masters);
void Animal_Hello(animal_t *this);
static void Animal_Counter();
int Animal_Masters(animal_t *this);
char *Animal_ToString(animal_t *this);
void Animal_Finalize(animal_t *this);

    
    fp_vtable_t animal_vtable[ANIMAL_METHODS] = {
    (fp_vtable_t)Object_Equals,
    (fp_vtable_t)Object_HashCode,
    (fp_vtable_t)Object_GetClass, 
    (fp_vtable_t)Animal_ToString,
    (fp_vtable_t)Animal_Finalize,
    (fp_vtable_t)Animal_Hello,
    (fp_vtable_t)Animal_Masters,
};

class_t animal_metadata = {"Animal", &object_metadata, sizeof(animal_t), animal_vtable};

/************************ Dog Init *******************************/
int flag_dog_load = UNLOAD; 

dog_t *Dog();
dog_t *Dog_Creat(class_t *class);
void Dog_Constructor(dog_t *dog);
void Dog_Hello(dog_t *this);
char *Dog_ToString(dog_t *this);
void Dog_Finalize(dog_t *this);

    
    fp_vtable_t dog_vtable[ANIMAL_METHODS] = {
    (fp_vtable_t)Object_Equals,
    (fp_vtable_t)Object_HashCode,
    (fp_vtable_t)Object_GetClass, 
    (fp_vtable_t)Dog_ToString,
    (fp_vtable_t)Dog_Finalize,
    (fp_vtable_t)Dog_Hello,
    (fp_vtable_t)Animal_Masters,
};


class_t dog_metadata = {"Dog", &animal_metadata, sizeof(dog_t), dog_vtable};


/************************ Cat Init *******************************/
int flag_cat_load = UNLOAD; 

cat_t *Cat();
cat_t *Cat_S(char *colors);
cat_t *Cat_Creat(class_t *class);
void Cat_Constructor(cat_t *cat);
void Cat_Constructor_S(cat_t *cat, char *colors);
char *Cat_ToString(cat_t *this);
void Cat_Finalize(cat_t *this);


    fp_vtable_t cat_vtable[ANIMAL_METHODS] = {
    (fp_vtable_t)Object_Equals,
    (fp_vtable_t)Object_HashCode,
    (fp_vtable_t)Object_GetClass, 
    (fp_vtable_t)Cat_ToString,
    (fp_vtable_t)Cat_Finalize,
    (fp_vtable_t)Animal_Hello,
    (fp_vtable_t)Animal_Masters,
};
    
class_t cat_metadata = {"Cat", &animal_metadata, sizeof(cat_t), cat_vtable};


/************************ LegendaryAnimal Init *******************************/
int flag_la_load = UNLOAD; 

la_t *LegendaryAnimal();
la_t *La_Creat(class_t *class);
void La_Constructor(la_t *la);
void La_Hello(la_t *this);
char *La_ToString(la_t *this);
void La_Finalize(la_t *this);

    
    fp_vtable_t la_vtable[ANIMAL_METHODS] = {
    (fp_vtable_t)Object_Equals,
    (fp_vtable_t)Object_HashCode,
    (fp_vtable_t)Object_GetClass, 
    (fp_vtable_t)La_ToString,
    (fp_vtable_t)La_Finalize,
    (fp_vtable_t)La_Hello,
    (fp_vtable_t)Animal_Masters,
};

class_t la_metadata = {"LegendaryAnimal", &cat_metadata, sizeof(la_t), la_vtable};



/************************ Main Init *******************************/
void Foo(animal_t *a);






/************************ Main *******************************/

int main()
{
    /*
    ObjectTest();
    MITest();
    */
    object_t *obj1 = Object();
    animal_t *animal = Animal();
    dog_t *dog = Dog();
    cat_t *cat = Cat();
    la_t *la = LegendaryAnimal();
    

    Animal_Counter();
    printf("%d\n",animal->ID);
    printf("%d\n",((animal_t*)dog)->ID);
    printf("%d\n",((animal_t*)cat)->ID);
    printf("%d\n",((animal_t*)la)->ID);

    {
        size_t i = 0 ;
        animal_t *array[] = 
        {
            (animal_t *)Dog(),
            (animal_t *)Cat(),
            (animal_t *)Cat_S("white"),
            (animal_t *)LegendaryAnimal(),
            (animal_t *)Animal()

        };

        for (i= 0; i < 5 ; ++i)
        {
            ((animal_hello_t)(((object_t *)array[i])->metadata->vtable[HELLO]))(array[i]);
            printf("%d\n", ((get_I_t)(((object_t *)array[i])->metadata->vtable[MASTERS]))(array[i]));
        }

            for (i= 0; i < 5; ++i)
        {
            Foo(array[i]);
            free(array[i]);

        }

    }

    free(obj1);
    free(animal);
    free(dog);
    free(cat);
    free(la);

    /*
     animal_t *animal2 = Animal_I(15);
    printf("num_masters for animal 2 : %d\n" ,
    ((animal_masters_t)animal->obj.metadata->vtable[ANIMAL_MASTERS])(animal2));
    */
   
    return(0);
}


/************************ Object func*******************************/
object_t *Object()
{
    object_t *obj = Object_Creat(&object_metadata);

    if(NULL == obj)
    {
        return(NULL);
    }

    Object_Constructor(obj);

    return(obj);


}
object_t *Object_Creat(class_t *class)
{
    object_t *obj = NULL;

    obj = calloc(1, class->size);
    obj->metadata = &object_metadata;

    return(obj);
}
void Object_Constructor(object_t *obj)
{
    ;
}
int Object_Equals(object_t *this, object_t *other)
{
  return( (this) == (other) );
}
int Object_HashCode(object_t *this)
{
    int res = 1 ;   
    char *name = this->metadata->class_name;
    size_t i = 0;
    size_t len = strlen(name);

    for(i = 0; i < len ; i++)
    {
        res = 17*res + (name[i] ^ (name[i] >> __CHAR_BIT__)); 
    }
    return (res);
}
class_t *Object_GetClass(object_t *this)
{
    return(this->metadata);
}
char *Object_ToString(object_t *this)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return(NULL);
    }
    sprintf(str, "%s@%x", 
    this->metadata->class_name, Object_HashCode(this));
    return(str);
}
void Object_Finalize(object_t *obj)
{
    free(obj);
    obj = NULL;
}

/************************ MutableInteger func *******************************/
mi_t *MutableInteger(int data)
{
    mi_t *mi = MI_Creat(&mi_metadata);

    if(NULL == mi)
    {
        return(NULL);
    }

    MI_Constructor(mi, data);

    return(mi);

}
mi_t *MI_Creat(class_t *class)
{
    mi_t *mi = NULL;

    mi = calloc(1, class->size);

    return(mi);
}
void MI_Constructor(mi_t *mi , int data)
{
    mi->obj.metadata = &mi_metadata;
    mi->value = data;
}
int MI_Equals(object_t *this, object_t *other)
{
  return( MI_Get_I(this) == MI_Get_I(other) );
}
int MI_HashCode(object_t *this)
{
    int res = 1 ;
    mi_t *mi = (mi_t*)this;
    char *name = mi->obj.metadata->class_name;
    size_t i = 0;
    size_t len = strlen(name);

    for(i = 0; i < len ; i++)
    {
        res = 17*res + (name[i] ^ (name[i] >> __CHAR_BIT__)); 
    }
    res += 17*mi->value;

    return (res);
}
void MI_Set_I(void *this, int data)
{
    mi_t *mi = (mi_t*)this;
    mi->value = data;
}
int MI_Get_I(void *this)
{   
    mi_t *mi = (mi_t*)this;
    return(mi->value);
}

/************************ Animal func *******************************/
animal_t *Animal()
{
    animal_t *animal = NULL;
  
    animal = Animal_Creat(&animal_metadata);
    Animal_Constructor(animal);

    return(animal);

}
animal_t *Animal_Creat(class_t *class)
{
    animal_t *animal = NULL;

    animal = calloc(1, class->size);
    if(NULL == animal)
    {
        return(NULL);
    }
    animal->obj.metadata = &animal_metadata;

    return(animal);
}
void Animal_Constructor(animal_t *animal)
{
    char *str = NULL;

    if(!flag_load)
    {
        printf("Static block Animal 1\n");
        printf("Static block Animal 2\n");
        flag_load = LOAD;
    }
    Object_Constructor((object_t*)animal);
    printf("Instance initialization block Animal\n");
   
    printf("Animal Ctor\n");
    animal->ID = ++Animal_counter;
    animal->num_legs = 5;
    animal->num_masters = 1;

    ( (animal_hello_t)((object_t *)animal)->metadata->vtable[HELLO])(animal);
    Animal_Counter();
    str = ((animal_tostring_t)((object_t *)animal)->metadata->vtable[TOSTRING])(animal);
    printf("%s\n",str);
    free(str);
    
    str = ((tostring_t)object_metadata.vtable[TOSTRING])(&animal->obj);
    printf("%s\n",str);
    free(str);
}
void Animal_Constructor_I(animal_t *animal , int masters)
{
    if(!flag_load)
    {
        printf("Static block Animal 1\n");
        printf("Static block Animal 2\n");
        flag_load = LOAD;
    }
    Object_Constructor((object_t*)animal);
    printf("Instance initialization block Animal\n");

    printf("Animal Ctor int\n");
    animal->ID = ++Animal_counter;
    animal->num_legs = 5;
    animal->num_masters = masters;
}
animal_t *Animal_I(int masters)
{
    animal_t *animal = NULL;
  
    animal = Animal_Creat(&animal_metadata);
    Animal_Constructor_I(animal, masters);

    return(animal);
}

void Animal_Hello(animal_t *this)
{
    printf("Animal Hello!\n");
    printf("I have %d legs\n",this->num_legs);

}
static void Animal_Counter()
{
    if(!flag_load)
    {
        printf("Static block Animal 1\n");
        printf("Static block Animal 2\n");
        flag_load = LOAD;
    }
    printf("%d\n",Animal_counter);
}
int Animal_Masters(animal_t *this)
{
    return(this->num_masters);
}
char *Animal_ToString(animal_t *this)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return(NULL);
    }
    sprintf(str, "Animal with ID: %d",this->ID);
    return(str);
}
void Animal_Finalize(animal_t *this)
{
    printf("finalize Animal with ID: %d", this->ID);
    ((finalize_t)object_metadata.vtable[FINALIZE])((object_t*)this);
}

/************************ Dog func *******************************/
dog_t *Dog()
{
    dog_t *dog = NULL;
    
    dog = Dog_Creat(&dog_metadata);
    Dog_Constructor(dog);
    
    return(dog);
}
dog_t *Dog_Creat(class_t *class)
{
    dog_t *dog = NULL;

    dog = calloc(1, class->size);
    if(NULL == dog)
    {
        return(NULL);
    }
    ((object_t*)dog)->metadata = &dog_metadata;

    return(dog);
}
void Dog_Constructor(dog_t *dog)
{
    if(!flag_dog_load)
    {
        printf("Static block Dog\n");
        flag_dog_load = LOAD;
    }
    Animal_Constructor_I( &(dog->super), 2);
    printf("Instance initialization block Dog\n");
    printf("Dog Ctor\n");
    dog->num_legs = 4;
}
void Dog_Hello(dog_t *this)
{   
    printf("Dog Hello!\n");
    printf("I have %d legs\n", this->num_legs);

}
char *Dog_ToString(dog_t *this)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return(NULL);
    }
    sprintf(str , "Dog with ID: %d" , this->super.ID);

    return(str);
}
void Dog_Finalize(dog_t *this)
{
    printf("finalize Dog with ID: %d" , this->super.ID);
    ((animal_finalize_t)animal_metadata.vtable[FINALIZE])((animal_t*)this);
}


/************************ Cat func *******************************/
cat_t *Cat()
{
    cat_t *cat = NULL;

    cat = Cat_Creat(&cat_metadata);
    Cat_Constructor(cat);

    return(cat);

}
cat_t *Cat_S(char *colors)
{
    cat_t *cat = NULL;

    cat = Cat_Creat(&cat_metadata);
    Cat_Constructor_S(cat , colors);

    return(cat);
}
cat_t *Cat_Creat(class_t *class)
{
    cat_t *cat = NULL;

    cat = calloc(1, class->size);
    if(NULL == cat)
    {
        return(NULL);
    }
    ((object_t*)cat)->metadata = &cat_metadata;

    return(cat);
}
void Cat_Constructor(cat_t *cat)
{
    Cat_Constructor_S(cat, "black");
    printf("Cat Ctor\n");

    cat->num_masters = 2;
}
void Cat_Constructor_S(cat_t *cat, char *colors)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return;
    }

    if(!flag_cat_load)
    {
        printf("Static block Cat\n");
        flag_cat_load = LOAD;
    }
    Animal_Constructor(&(cat->super));
    sprintf(str, "%s",colors);
    cat->colors = str;
    printf("Cat Ctor with color: %s\n" , cat->colors);
    cat->num_masters = 5;

}
char *Cat_ToString(cat_t *this)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return(NULL);
    }
    sprintf(str , "Cat with ID: %d" , this->super.ID);
    
    return(str);
}
void Cat_Finalize(cat_t *this)
{
     printf("finalize Cat with ID: %d" , this->super.ID);
    ((animal_finalize_t)animal_metadata.vtable[FINALIZE])((animal_t*)this);
}

/************************ LegendaryAnimal func *******************************/
la_t *LegendaryAnimal()
{
    la_t *la = NULL;

    la = La_Creat(&la_metadata);
    La_Constructor(la);

    return(la);
}
la_t *La_Creat(class_t *class)
{
    la_t *la = NULL;

    la = calloc(1, class->size);
    if(NULL == la)
    {
        return(NULL);
    }
    ((object_t*)la)->metadata = &la_metadata;

    return(la);
}
void La_Constructor(la_t *la)
{
    if(!flag_la_load)
    {
        printf("Static block Legendary Animal\n");
        flag_la_load = LOAD;
    }
    Cat_Constructor(&(la->super));
    printf("Legendary Ctor\n");
}
void La_Hello(la_t *this)
{
     printf("Legendary Hello!\n");
}
char *La_ToString(la_t *this)
{
    char *str = malloc(BUFFER_SIZE);
    if(NULL == str)
    {
        return(NULL);
    }
    sprintf(str , "LegendaryAnimal with ID: %d" , this->super.super.ID);
    
    return(str);
}
void La_Finalize(la_t *this)
{
     printf("finalize LegendaryAnimal with ID: %d" , this->super.super.ID);
    ((animal_finalize_t)cat_metadata.vtable[FINALIZE])((animal_t*)this);
}



/************************ Test func *******************************/
void ObjectTest()
{
    object_t *obj1 = Object();
    object_t *obj2 = Object();
    char *str = NULL;

    printf("obj1 hash is: %d\n", ((hashCode_t)obj1->metadata->vtable[HASH])(obj1));
    printf("obj2 hash is: %d\n", ((hashCode_t)obj2->metadata->vtable[HASH])(obj2));

    printf("obj1.equals(obj2) is: %d\n", ((equals_t)obj1->metadata->vtable[EQUALS])(obj1, obj2));
    printf("obj1.equals(obj1) is: %d\n", ((equals_t)obj1->metadata->vtable[EQUALS])(obj1, obj1));

    str =  ((tostring_t)obj1->metadata->vtable[TOSTRING])(obj1);
    

    ((finalize_t)obj1->metadata->vtable[FINALIZE])(obj1);

    ((finalize_t)obj2->metadata->vtable[FINALIZE])(obj2);
    
    free(str);

}

void MITest()
{
    mi_t *mi1 = MutableInteger(13);
    mi_t *mi2 = MutableInteger(-7);

    printf("creation SUCCESS, mi1.value = %d\n", mi1->value);
    printf("creation SUCCESS, mi2.value = %d\n", mi2->value);

    printf("mi1.getValue is: %d\n", (MI_Get_I(mi1)));
    printf("mi1.equals(mi2) is: %d\n", 
    ( (equals_t)mi1->obj.metadata->vtable[MI_EQUALS])((object_t *)mi1, (object_t *)mi2));
    ((set_I_t)mi1->obj.metadata->vtable[MI_SET])(mi1, -7);
    printf("mi1.getValue is: %d\n", (MI_Get_I(mi1)));
    printf("mi1.equals(mi2) is: %d\n", 
    ( (equals_t)mi1->obj.metadata->vtable[MI_EQUALS])((object_t *)mi1, (object_t *)mi2));

    free(mi1);
    free(mi2);
}


void Foo(animal_t *a)
{
    char *str = NULL;

    str = ((animal_tostring_t)(((object_t *)a)->metadata->vtable[TOSTRING]))((animal_t *)a);
    printf("%s\n", str);
    free(str);
}






