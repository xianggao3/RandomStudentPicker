public class ActualVsApparentExample 
{
    public static void main(String[] args){
        Person p = new Person();
        p.firstName = "Joe";
        p.lastName = "Shmo";
        print(p);
        p = new Student();
        p.firstName = "Jane";
        p.lastName = "Doe";
        print(p);
        Student s = (Student)p;
        print(s);
        
    }
    
    public static void print(Person p){
        System.out.println(p);
    }
}

class Person 
{
    public String firstName;
    public String lastName;
    public String toString()
    {	return firstName + " " + lastName;		}
}

class Student extends Person
{
    public double GPA;
    public String toString()
    {	return "" + GPA;					}
}   
