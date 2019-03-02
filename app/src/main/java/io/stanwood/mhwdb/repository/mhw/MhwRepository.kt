package io.stanwood.mhwdb.repository.mhw

import com.nytimes.android.external.fs3.SourcePersisterFactory
import com.nytimes.android.external.fs3.filesystem.FileSystem
import com.nytimes.android.external.store3.base.Parser
import com.nytimes.android.external.store3.base.impl.BarCode
import com.nytimes.android.external.store3.base.impl.MemoryPolicy
import com.nytimes.android.external.store3.base.impl.StoreBuilder
import io.reactivex.Single
import io.stanwood.framework.network.store.SerializationParserFactory
import io.stanwood.mhwdb.datasource.net.mhw.MhwApi
import io.stanwood.mhwdb.datasource.net.mhw.MhwArmor
import io.stanwood.mhwdb.datasource.net.mhw.MhwWeapon
import kotlinx.serialization.list
import okio.BufferedSource
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("UnstableApiUsage")
@Singleton
class MhwRepository @Inject constructor(private val api: MhwApi, fileSystem: FileSystem) {
    companion object {
        private val allWeapon = BarCode("Weapon", "all")
        private val allArmor = BarCode("Armor", "all")
    }

    private val sourcePersister = SourcePersisterFactory.create(fileSystem, 60, TimeUnit.MINUTES)
    private val memoryPolicy =
        MemoryPolicy.builder().setExpireAfterWrite(30).setExpireAfterTimeUnit(TimeUnit.MINUTES)
            .build()

    private val weaponStore by lazy {
        SerializationParserFactory.createSourceParser(MhwWeapon.serializer().list)
            .fetchFrom { api.fetchWeapon() }
            .open()
    }

    private val armorStore by lazy {
        SerializationParserFactory.createSourceParser(MhwArmor.serializer().list)
            .fetchFrom { api.fetchArmor() }
            .open()
    }

    fun fetchWeaponByType(weaponTypeId: String): Single<List<Weapon>> =
        weaponStore.get(allWeapon)
            .map { src -> src.filter { it.type == weaponTypeId }.map { it.mapToWeapon() } }

    fun fetchWeaponTypes(): Single<List<WeaponType>> =
        weaponStore.get(allWeapon)
            .map { src ->
                src.distinctBy { it.type }.map { it.mapToWeaponType() }
            }

    fun fetchArmorSets(): Single<List<ArmorSet>> =
        armorStore.get(allArmor).map { src ->
            src.distinctBy { it.armorSet?.id ?: it.id }
                .map { armor ->
                    armor.armorSet?.pieces
                        ?.mapNotNull { id ->
                            src.find { it.id == id }?.mapToArmor()
                        }?.let {
                            armor.armorSet.mapToArmorSet(it)
                        }
                        ?: ArmorSet(armor.id, armor.name, armor.rank, listOf(armor.mapToArmor()))
                }
        }

    fun fetchArmorById(id: Long): Single<Armor> =
        armorStore.get(allArmor).map { src ->
            src.first { it.id == id }
                .mapToArmor()
        }

    private fun <T> Parser<BufferedSource, T>.fetchFrom(fetcher: (BarCode) -> Single<BufferedSource>) =
        StoreBuilder.parsedWithKey<BarCode, BufferedSource, T>()
            .fetcher(fetcher)
            .persister(sourcePersister)
            .refreshOnStale()
            .memoryPolicy(memoryPolicy)
            .parser(this)
}