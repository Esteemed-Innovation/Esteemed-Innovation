# Contributing
We love when people help us do our job for us. Here is some information on how you can do that.

## Issues
Creating issues is the most basic way to help us develop the mod. Issues can be created for various reasons, for example: you found a bug that should be reported or you have a feature idea.

According labels will be added to your issues as soon as an active contributor sees it (typically @elifoster or @xbony2).

### Bug reporting
Bug reports should include the Minecraft Forge version, the Flaxbeard's Steam Power version, and a detailed explanation of the bug. This explanation should *generally* include:
- When it happens
- The blocks/items involved
- Any extra mods that may be interfering

### Feature suggesting
Feature suggestions are pretty loose. They should be as detailed as the feature is complicated.

## Pull requests
Pull requests can be made to submit code snippets to the mod to either fix bugs, add features, or just generally improve it. Pull requests should always match the style guide (below), and ones that do not will be rejected.

## Commit messages
We're pretty lenient about these guidelines, so it's not a huge deal if it's not followed. However, following it definitely improves the cleanliness of the project, and promotes good style! Try your best to follow this!
* Use the present tense (fix not fixed).
* Use the imperative mood (fix not fixes).
* Limit the first line to 72 characters or less. If you need to provide more information, use the first line for a summary, then describe in great detail in the next lines.
* Mention issues and pull requests liberally, unless closing or "opening" (GitHub does not support re-opening issues in commits, but if you state "Open #5" and then manually open it, it will achieve the same thing).
* Consider prefixing your commits with emojis:
  * :art: `:art:` when dealing with textures, either loading them or adding new ones.
  * :saxophone: `:saxophone:` when dealing with sounds, either loading them or adding new ones.
  * Any of the earths (:earth_africa: :earth_asia: :earth_americas: :globe_with_meridians: `:earth_africa: :earth_asia: :earth_americas: :globe_with_meridians:`) when dealing with localization.
  * :hatching_chick: `:hatching_chick:` when finishing an unfinished feature.
  * :seedling: `:seedling:` when starting a feature, but not finishing it.
  * :fu: `:fu:` when fixing code format or style.
  * :birthday: `:birthday:` when updating the mod version number.
  * :arrow_up: `:arrow_up:` when upgrading dependency versions.
  * :arrow_down: `:arrow_down:` when downgrading dependency versions.
  * :white_check_mark: `:white_check_mark:` when adding tests.
  * :broken_heart: `:broken_heart:` when deleting code or files.
  * :apple: `:apple:` when dealing with OS X specifically.
  * :penguin: `:penguin:` when dealing with Linux specifically.
  * :checkered_flag: `:checkered_flag:` when dealing with Windows specifically.
  * :racehorse: `:racehorse:` when improving performance.
  * :non-potable_water: `:non-potable_water:` when fixing memory leaks.
  * :book: `:book:` when updating or adding docs.
  * :bug: `:bug:` when fixing a bug.
  * :shirt: `:shirt:` when resolving linter warnings.
  * :gem: `:gem:` when adding a fully fleshed out feature.
  * :wrench: `:wrench:` when tweaking a feature.
  * :moneybag: `:moneybag:` when improving balance.
  * :notebook: `:notebook:` when adding Esteemed Innovation journal entries.

You are not required to use emojis, but it is allowed and encouraged, as it allows for quickly knowing the purpose of a commit.

# Java Style Guide
**Please note that this style guide is still being implemented in the codebase, and code may or may not actually follow it. If you see code that does not follow it, please let a developer know, or simply submit a pull request. Thanks.**

This Java Style Guide applies to Flaxbeard's Steam Power, and associated projects. All pull requests must meet this criteria for them to be merged with master. This Style Guide applies to Java only; Groovy, Scala, CSS, and any other languages that Flaxbeard's Steam Power may have in its associated projects are not effected. This Style Guide is a modified version of the [Google Java Style Guide](http://google-styleguide.googlecode.com/svn/trunk/javaguide.html).

## Source file basics

## File name
The source file name consists of the PascalCased or UpperCamelCased name of the top-level class it contains, plus the `.java` extension.

## Special characters
The actual Unicode character or the equivalent Unicode escape character should be used in cases where necessary, depending on which makes the code easier to read and understand. If the actual Unicode character is not used, and the Unicode escape character is used, a comment should suffix the line of code explaining what that character is.

## Source file structure
A source file consists of, in order:

1. Package statement
2. Import statements
3. Two or less top-level classes

### Package statement
The package statement is not line-wrapped.

### Import statements
#### Wildcards
Wildcard imports are allowed if a large majority of the files in the wildcarded package are required for the class.

#### Line-wrapping
Import statements must not be line-wrapped.

#### Ordering and spacing
Import statements are divided into the following groups, in this order, with each group separated by a single black line:

1. All static imports in a single group
2. `flaxbeard.steamcraft` imports
3. Third-party imports, for example, from Minecraft Forge or Minecraft.
4. `java` imports
5. `javax` imports

### Class declaration
#### Two or less top-level class declaration
In most cases, each top-level class resides in a source file of its own. If the use of having its own source file is debatable, this could be ignored. No more than two top-level classes.

#### Class member ordering
Class members are ordered in a logical order. This order is *not* chronilogical; that is not logical. The author must be able to explain the order of the members.

##### Overloads
Multiple methods of the same name or multiple constructors appear sequentially with no intervening members.

## Formatting
### Braces
#### Braces are used where optional
Braces are used with `if`, `else`, `for`, `do`, and `while` statements, even when the body is empty or contains only a single statement.

#### Nonempty blocks
For nonempty blocks and block-like constructs:
- No line break before the opening brace
  - Space before the opening brace
- Line break after the opening brace
- Line break before the closing brace
- Line break after the closing brace *if* that brace terminates a statement or the body of a method, constructor, or *named* class. For example, there is *no* line break after the brace if it is followed by `else`.

#### Empty blocks
An empty block or block-like construct should be closed immediately after it is opened, with no characters or line break in between (`{}`), unless it is part of a multi-block statement that directly contains multiple blocks (`if`/`else-if`/`else`/`try`/`catch`/`finally`).

### Block indentation
Each time a new block or block-like construct is opened, the indent increases by 4 spaces. When the block ends, the indent returns to the previous indentation level. The ident level applies to both code and comments throughout the block.

### One statement per lane
Each statement is followed by a line break.

### Column limit
Any line that would exceed the limit of 120 characters must be line-wrapped.

Exceptions:

1. Lines where obeying the column limit is impossible (for example, long URL in Javadoc)
2. `package` and `import` statements
3. Command lines in a comment that may be cut-and-pasted into a shell.
4. Parameters listed in method declaration (for example, `public void method(int var1, int var2) {`)
5. Class declarations (for example `public class A extends B implements C, D, E, F... {`)

### Line-wrapping
#### Where to break

1. When a line is broken at a non-assignment operator the break comes before the symbol.
  - This applies to the following operator-like symbols: ., &, |
2. When a line is broken at an assignment operator the break typically comes after the symbol.
  - This also applies to the assignment-operator-like colon in an enhanced `for` statement.
3. A comma stays attached to the token that precedes it.

#### Indent continuation lines
When line-wrapping, each line after the first (each continuation line) is indented +2 spaces. Two continuation lines for the same statement use the same indentation level.

### Whitespace
#### Vertical whitespace
A single blank line appears:
- Between consecutive members of a class: fields, constructors, methods, nested classes, static initializers, instance initializers.
  - Exception: A blank line between two consecutive fields is optional. Such blank lines are used as needed to create logical groupings of fields. Logical grouping is never chronilogical.
- Within method bodies, as needed to create logical groupings of statements.
- Optionally before the first member or after the last member of the class.
- As required by other sections of this style guide.

#### Horizontal whitespace
Beyond where required by the language or other style rules, and apart from literals, comments, Javadoc, a single ASCII space also appears in the following places *only*:
- Separating any reserved word, such as `if`, `for`, or `catch` from an open parenthesis that followes it on that line
- Separating any reserved word, such as `else` or `catch` from a closing curly brace that precedes it on that line.
- Before any open curly brace, with two exceptions
  - @SomeAnnotation({foo, bar}) (no space used)
  - String[][] x = {{"foo"}}; (no space is required between {{)
- On both sides of any binary or ternary operator. This also applies to the following operator-like symbols:
  - & in connjunctive type bound
  - | for a catch block that handles multiple exceptions
  - : in an enhanced `for` statement
- After `,:'` or the closing parenthesis of a cast
- On both sides of the double slash that begins an end-of-line comment. Having multiple spaces are allowed but not required.
- Between the type and variable of a declaration.
- Optionally inside both braces of an array initializer
  - `new int[] {5, 6}` and `new int[] { 5, 6 }` are both valid

This rule never requires or forbids additional space at the start or end of a line, only interior space.

#### Horizontal alignment
Horizontal alignment is the practice of adding a variable amount of additional spaces in your code with the goal of making certain tokens appear directly below certain other tokens on previous lines.

This practice is permitted and encouraged, but not required.

### Grouping parentheses
Optional grouping parentheses are omitted only when author and reviewer agree that there is no reasonable chance the code will be misinterpreted without them, nor would they have made the code easier to read. It is not reasonable to assume that every reader has the entire Java operator precedence table memorized.

### Specific constructs
#### Enums
After each comma that follows an enum constant, a line break is required.

Since enum classes are classes, all other rules for formatting classes apply.

#### Variable declaration
A couple of rules are in place for variable delcaration constructs:
1. Every variable declaration declares only one variable: declarations such as `int a, b;` are not used.
2. Declared when needed, initialized as soon as possible. This is in place to minimize the variable's scope.

#### Arrays
##### Array initializers
Array initializers can be block-like. This is optional.

##### C-style
Array declarations must not be C-style; the square brackets form a part of the type, not the variable: `String[] args`, not `String args[]`.

#### Switch statements
##### Indentation
As with any other block, the contents of a switch block are indented +4.

After a switch label, a newline appears, and the indentation level is increased +4, exactly as if a block were being opened. The following switch label returns to the previous indentation level, as if a block had been closed.

##### Fall-through
Within a switch block, each statement group either terminates abruptly (with a `break`, `continue`, `return` or thrown exception), or is marked with a comment to indicate that execution will or might continue into the next statement group. Any comment that communicates the idea of fall-through is sufficient (typically // fall through). This special comment is not required in the last statement group of the switch block.

##### Default case
The default case is always present, even if it contains no code.

### Annotations
Each annotation is listed on a line of its own. A set of annotations is always directly above the method, class, or constructor that they apply to. These line breaks do not constitute line wrapping. The indentation level is never increased with multiple annotations.

Parameterless annotations must still have their own line.

### Comments
#### Block comments
Block comments are indented at the same level as the surrounding code. They may be in `/* ... */` style or `// ...` style. For multi-line `/* ... */` comments, subsequent lines must start with `*` aligned with the `*` on the previous line.

Comments are not enclosed in boxes drawn with asterisks or other characters.

### Modifiers
Class and member modifiers, when present, appear in the order recommended by the Java Language Specification:

`public protected private abstract static final transient volatile synchronized native strictfp`

### Numeric literals
`long` integer literals use an uppercase `L` suffix, never lowercase, to avoid confusion with the digit `1`.

## Naming
### Rules common to all identifiers
Identifiers use only ASCII letters and digits, and in two cases noted below, underscores. Thus each valid identifier name is matched by the regular expression `\w+`.

### Rules by identifier type
#### Package names
Package names are all lowercase, with consecutive words simply concatenated together with no underscores.

#### Class names
Class names are written in UpperCamelCase.

Class and interface names are typically nouns or noun phrases. Interface names may sometimes be adjectives or adjective phrases.

Test classes are named statring with the name of the class they are testing, ending with `Test`.

#### Method names
Method names are written in lowerCamelCase.

Method names are typically verbs or verb phrases.

#### Constant names
Constant names use CONSTANT_CASE: all uppercase letters, with words separated by underscores. But what is a constant, exactly?

Every constant is a static final field, but not all static final fields are constants. Before choosing constant case, consider whether the field really feels like a constant. For example, if any of that instance's observable state can change, it is almost certainly not a constant. Merely intending to never mutate the object is generally not enough.

Constant names are typically nouns or noun phrases.

#### Non-constant field names
Non-constant field names are written in lowerCamelCase.

These names are typically nouns or noun phrases.

#### Parameter names
Parameter names are written in lowerCamelCase.

One-character parameter names should be avoided.

Obfuscated or incomprehensible parameter names are never allowed. Ever.

Parameter names that are used less should be significantly more descriptive than those that are used frequently. This is optional.

Non-self-explanatory parameter names require documentation in the form of a comment.

#### Local variable names
Local variable names are written in lowerCamelCase, and can be abbreviated more liberally than other types of names.

However, one-character names should be avoided, except for temporary and looping variables.

Even when final and immutable, local variables are not considered to be constants, and should not be styled as constants.

#### Type variable names
Each type variable is named in one of two styles:

- A single capital letter, optionally followed by a single numeral (such as E, T, X, T2)
- A name in the form used for classes (see Section 5.2.2, Class names), followed by the capital letter T (examples: RequestT, FooBarT).

## Programming practices
### @Override
The `@Override` annotation marks methods in every single legal case, except when the parent method is `@Deprecated`.

### Caught exceptions
Apart from tests, caught exceptions should not be ignored.

### Static members
When a reference to a static class member must be qualified, it is qualified with that class's name, not with a reference or expression of that class's type.

### Finalizers
Don't use finalizers.
