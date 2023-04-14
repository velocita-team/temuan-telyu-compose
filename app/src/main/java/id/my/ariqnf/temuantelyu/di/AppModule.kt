package id.my.ariqnf.temuantelyu.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.my.ariqnf.temuantelyu.domain.HomeRepository
import id.my.ariqnf.temuantelyu.domain.ProfileRepository
import id.my.ariqnf.temuantelyu.domain.impl.HomeRepositoryImpl
import id.my.ariqnf.temuantelyu.domain.impl.ProfileRepositoryImpl
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
    fun providesHomePostRepository(firebaseFirestore: FirebaseFirestore): HomeRepository {
        return HomeRepositoryImpl(firebaseFirestore)
    }

    @Provides
    @Singleton
    fun providesProfileRepository(firestore: FirebaseFirestore): ProfileRepository =
        ProfileRepositoryImpl(firestore)
}