package cz.idlgs.mobile.di

import cz.idlgs.mobile.data.repository.AuthRepositoryImpl
import cz.idlgs.mobile.data.repository.OpenAIServerRepositoryImpl
import cz.idlgs.mobile.domain.repository.AuthRepository
import cz.idlgs.mobile.domain.repository.ChatRepository
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
	fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl()

	@Provides
	@Singleton
	fun provideChatRepository(): ChatRepository = OpenAIServerRepositoryImpl()
}
