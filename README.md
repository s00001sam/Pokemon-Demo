功能
- 依照 Type 顯示 Pokemon 列表，總共 151 種 Pokemon
- 本地儲存 Pokemon 資訊及圖片，可離線觀看列表
- 點擊 列表 - 下方的 Pokemon 愛心可以加入捕捉（My Pocket），且可以重複捕捉相同 Pokemon
- 點擊 列表 - My Pocket 的 Pokemon 愛心可以移除捕捉（My Pocket）
- 點擊 列表 - My Pocket 的標題可以打開及收起捕捉列表
- 點擊 列表的 Pokemon 可以觀看詳細資訊
- 點擊 詳細資訊的 evolvesFrom Pokemon 可以觀看其詳細資訊
- 支援下拉刷新
- 支援 darkMode

列表頁面 | 詳細頁面
--- | ---
<video src="https://github.com/user-attachments/assets/e0601797-f7a9-4f24-a815-3d1274cb65de"> | <video src="https://github.com/user-attachments/assets/1033eb74-133b-4ef9-b66c-d34e3c95fd6b">


其餘實作
- Room 及 SharedPreferences 實現本地儲存
- Retrofit & OkHttp3 串接 API
- Hilt 依賴注入
- Coil 實現圖片載入 及 disk cache
- Compose 實作 UI
- Compose Navigation 實現頁面跳轉
- Compose Preview
- Unit test for UseCase

本地儲存邏輯
- 第一次進入列表頁面會將 Pokemon 基本資料及 Type 存入資料庫，並 cache 圖片
- 若第一次資料未完全載入就離開 APP，下次進入 APP 會只請求未載完的資料
- 每次 Cache 圖片成功會存入 imageCache 表，代表圖片有下載成功
- 未在 imageCache 裡的圖片，才會進行下載
- 列表頁面第一次成功取得所有 Pokemon 基本資料及 Type 後，之後再進入 APP 就不會主動呼叫 API，除非使用者刷新
- Pokemon 詳細資料，會在進入詳細頁面，才進行請求及儲存

API
- pokeapi v2 API
- [GET] https://pokeapi.co/api/v2/pokemon?limit=151
    - 取得 Pokemon 名稱及基本資訊 Url
- [GET] https://pokeapi.co/api/v2/pokemon/{id or name}
    - 取得單一 Pokemon 基本資訊（id, name, imageUrl, types)
- [GET] https://pokeapi.co/api/v2/pokemon-species/{id or name}
    - 取得單一 Pokemon 額外資訊（evolution information, description）

資料庫（使用 Room 做資料庫儲存）
- Entity
    - Pokemon：Pokemon 資訊
    - Type：Type 資訊
    - Capture：捕捉基本資訊
    - imageCache：已 Cache 的圖片
    - typePokemonCrossRef：Type 與 Pokemon 的關聯表
- View
    - capture_pokemon_view：捕捉的 Pokemon
    - detail_pokemon_view：Pokemon 詳細資訊（包含 evolvesFrom 的相關資訊）
- 其餘 Model
    - DetailPokemonWithTypes：Pokemon 詳細資訊及對應的 TypeName 們
    - TypeWithPokemons：Type 及對應的 Pokemon 們

架構
- MVVM
- 嘗試從 Repository 分離出 UseCase，讓 Repository 變成單純統整及供應資料的類別，將商業邏輯及 UI Model 的轉換放進 UseCase
 
- DataSource：資料來源
    - BaseDataSource：資料來源的介面
    - RemoteDataSource：遠端資料來源
    - LocalDataSource：本地資料來源
      
- Repository：統整資料來源
    - BaseRepository：統整資料來源的 repository 介面
    - PokemonDataSource：統整 Pokemon 相關的不同資料來源
      
- UseCase：基於使用者流程或操作，使用各資料來源，轉換成 UI Model 或 Flow，並可包含商業邏輯
    - UpdatePokemonsFromRemoteUseCase 更新本地資料庫的相關邏輯（Pokemon 基本資訊）
    - GetTypeWithPokemonsUseCase 取得種類和 Pokemon 們組成的列表
    - InsertCaptureUseCase 在捕捉列表加入 Pokemon
    - DeleteCaptureByIdUseCase 在捕捉列表刪除 Pokemon
    - GetCapturedPokemonsUseCase 取得捕捉的 Pokemon 列表
    - GetDetailPokemonUseCase 取得詳細的 Pokemon 資訊
    - UpdatePokemonDetailFromRemoteUseCase 更新本地資料庫的相關邏輯（Pokemon 詳細資訊）
      
- ViewModel：處理畫面邏輯，以及存放當前的畫面資訊及 Flow
    - HomeViewModel
    - DetailViewModel
      
- View：畫面呈現，使用 Compose，收集來自 ViewModel 的 Flow
    - MainActivity
        - MyRouter（NavHost）
            - HomeScreen
            - DetailScreen

<img width="866" alt="image" src="https://github.com/user-attachments/assets/029eceae-e745-49bc-8985-11211528eea3" />
