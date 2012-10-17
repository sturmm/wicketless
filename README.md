wicketless
==========

{less} (see http://www.lesscss.org for more information) integration for Wicket 1.5. using Mozilla's Rhino engine for runtime less compilation. Other wicket versions are in Progress.

Use it like every other css resource reference:
```java
public void renderHead(IHeaderResponse response) {
	...
	response.renderCSSReference(new LessCssResourceReference(HomePage.class, "HomePage.less"));
}
```

You can simply Unit-Test your script:

```java
@Test
public void test() {
	new LessSource(Foo.class, "Bar.less").toCSS();
}
```    

Meaningfull Errors
----------
Error messages are printed as detailed as possible e.g:
```
org.sturmm.wicketless.less.ParsingError: { 
	file: 'org/sturmm/wicketless/Foo.less', 
	message: org.sturmm.wicketless.less.ParsingError: { 
		file: 'org/sturmm/wicketless/Import.less', 
		error: { line:4, column: 5}, 
		source:{ 
			3 | 
			4*| .fo(o(){
			5 |  height: 15px;
		},
		message: expected ')' got '('
	}
}
```
It's tree like structure containing line, column and code extract for simply finding and solving issues.

Less Imports
----------
We support two types of imports. At the one side you can add relative references to your less code and at the other hand you can specify an import by using the fully qualified name of a classpath resource by adding 'classpath:' as prefix:

```lesscss
@import "classpath:foo/bar/baz";
@import "../foo/bar.less";
@import "foo/bar.less";
```

As you can see in first line you must not use the '.less' prefix. It's added automatically.

Simple Customization
----------
Adopt the framework for your needs. Provide your way for loading less sources (e.g. database, cms, http ...) by extending AbstractLessSource:

```java
public class DbLessSource extends AbstractLessSource
{

	@Override
	public LessSource resolveImport(String filename)
	{
		return new DbLessSource(filename);
	}

	@Override
	public String getFilename()
	{
		return filename;
	}

	@Override
	public String getSource()
	{
		return service.loadSourceById(filename);
	}

	@Override
	public boolean isCompressed()
	{
		return false;
	}

}
`` 


Caching
----------
At the moment we're using Wicket's ResourceReferenceRegistry for caching, but only if you are in Deployment mode. ResourceReferenceRegistry supports max 1000 of all resources by default! In development mode LessCss resource will be compiled on each request.

Or quite you use LessCssResourceReference as static constant, so it will be compiled only once, if you are in Deployment mode:
```java
private static final ResourceReference CSS = new LessCssResourceReference(HomePage.class, "HomePage.less");
    
public void renderHead(IHeaderResponse response) {
	...
	response.renderCSSReference(CSS);
}
```
In development mode the LessResource will be instantiated new for each request, so that you can see your changes immediately.


Build
----------
Simply checkout and than type "mvn clean install" ;)

TODO
----------

Still under development. 
- more Caching
- Wicket 6 support
- release

License
----------

Copyright [2012] [Martin Sturm]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

