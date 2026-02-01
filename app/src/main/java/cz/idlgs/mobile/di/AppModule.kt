package cz.idlgs.mobile.di

import cz.idlgs.mobile.data.repository.AuthRepositoryReal
import cz.idlgs.mobile.domain.repository.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

	@Provides
	@Singleton
	fun provideAuthRepository(): AuthRepository = AuthRepositoryReal()
}
