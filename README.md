# Binary Decision Diagram (BDD) Implementation in Java

This Java code provides an implementation of a Binary Decision Diagram (BDD), a data structure used for representing boolean functions. The BDD is constructed based on a given boolean expression and variable order.

## Overview

The `BDD` class represents the Binary Decision Diagram, which is created from a boolean expression and a specified variable order. The BDD is a tree-like structure with nodes representing boolean variables and decision points.

## Usage

### 1. Create a BDD Object:

```java
BDD bdd = new BDD(expression, order);
```
The constructor takes a boolean expression and a variable order as parameters to create the BDD.
### 2. BDD Operations:
Insertion: The BDD is constructed during initialization based on the given expression and order.

Print BDD Structure:
    
    bdd.print();

This method prints the structure of the BDD, displaying nodes, their conditions, and child nodes.

#### Evaluate BDD:

```java
String result = bdd.use(input);
```
This method evaluates the BDD for a given set of inputs and returns the result.

### 3. Additional Information:

Get Variable Order:

```java
String order = bdd.getOrder();
```
This method returns the variable order used in the BDD.


Get Number of Nodes:
```java
int numberOfNodes = bdd.getNodesSize();
```
This method returns the number of nodes in the BDD.

## Example

```java
public static void main(String[] args) {
    String expression = "A * B + !C";
    String order = "ABC";
    
    BDD bdd = new BDD(expression, order);
    bdd.print();

    String input = "101";
    String result = bdd.use(input);
    System.out.println("BDD Evaluation for Input " + input + ": " + result);
}
```