package id.my.ariqnf.temuantelyu.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.my.ariqnf.temuantelyu.domain.AuthRepository
import id.my.ariqnf.temuantelyu.domain.ChatRepository
import id.my.ariqnf.temuantelyu.domain.HomeRepository
import id.my.ariqnf.temuantelyu.domain.PostRepliesRepository
import id.my.ariqnf.temuantelyu.domain.PostRepository
import id.my.ariqnf.temuantelyu.domain.ProfileRepository
import id.my.ariqnf.temuantelyu.domain.SearchRepository
import id.my.ariqnf.temuantelyu.domain.impl.AuthRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.ChatRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.HomeRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.PostRepliesRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.PostRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.ProfileRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.SearchRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun providesFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun providesAuthRepository(
        firebaseAuth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository =
        AuthRepositoryImpl(firebaseAuth, firestore)

    @Provides
    @Singleton
    fun providesHomePostRepository(firebaseFirestore: FirebaseFirestore): HomeRepository =
        HomeRepositoryImpl(firebaseFirestore)

    @Provides
    @Singleton
    fun providesProfileRepository(firestore: FirebaseFirestore): ProfileRepository =
        ProfileRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun providesPostRepository(
        storage: FirebaseStorage,
        firestore: FirebaseFirestore
    ): PostRepository = PostRepositoryImpl(storage, firestore)

    @Provides
    @Singleton
    fun providesPostRepliesRepository(
        firestore: FirebaseFirestore,
    ): PostRepliesRepository = PostRepliesRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun providesSearchRepository(firestore: FirebaseFirestore): SearchRepository =
        SearchRepositoryImpl(firestore)

    @Provides
    @Singleton
    fun providesChatRepository(firestore: FirebaseFirestore): ChatRepository =
        ChatRepositoryImpl(firestore)
}