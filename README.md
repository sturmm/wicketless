wicketless
==========

LessCSS integration for Wicket 1.5. using Mozilla's Rhino engine for runtime less compilation. Other wicket versions are in Progress.

Use it like every other css resource reference:
```java
	public void renderHead(IHeaderResponse response) {
		...
	    response.renderCSSReference(new LessCssResourceReference(LessCompiler.class, "HomePage.less"));
    }
```

Caching
----------
At the moment we're using Wicket's ResourceReferenceRegistry for caching, but only if you are in Deployment mode. ResourceReferenceRegistry supports max 1000 of all resources by default! In development mode LessCss resource will be compiled on each request.

Less Imports
----------
We support two types of imports. At the one side you can add relative references to your less code and at the other hand you can specify an import by using the fully qualified name of a classpath resource by adding 'classpath:' as prefix:

```lesscss
@import "classpath:foo/bar/baz";
@import "../foo/bar.less";
```

As you can see you must not use the '.less' prefix. It's added automatically.

Build
----------
Simply checkout and than type "mvn clean install" ;)

TODO
----------

Still under development. 
- extract less compiler in extra project
- Add css-import support
- Caching
- Cleanup
- Documentation
- UnitTests 
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

