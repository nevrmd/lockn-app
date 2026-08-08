# Room, Hilt, and kotlinx.datetime/coroutines ship their own consumer ProGuard rules
# (bundled in their AARs) and don't need explicit rules here. Verified by manually
# installing and exercising a release build after enabling minification.

# kotlinx.serialization is used for the type-safe Nav-Compose `Route` sealed interface
# (core:navigation). Keep the generated serializers so nav-arg (de)serialization survives
# R8 — this is a known gotcha for sealed interfaces specifically, since their serializer
# lookup happens via reflection at decode time rather than a static call site.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keepclassmembers class com.nevrmd.navigation.** {
    *** Companion;
}
-if @kotlinx.serialization.Serializable class com.nevrmd.navigation.**
-keepclassmembers class <1>$Companion {
    kotlinx.serialization.KSerializer serializer(...);
}
-if @kotlinx.serialization.Serializable class com.nevrmd.navigation.**
-keepclassmembers class <1> {
    *** Companion;
}
