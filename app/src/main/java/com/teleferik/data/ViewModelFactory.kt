package com.teleferik.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.teleferik.base.BaseRepo
import com.teleferik.ui.auth.AuthRepo
import com.teleferik.ui.auth.AuthViewModel
import com.teleferik.ui.dashboard.DashboardRepo
import com.teleferik.ui.dashboard.DashboardViewModel
import com.teleferik.ui.home.HomeRepo
import com.teleferik.ui.home.HomeViewModel
import com.teleferik.ui.more.ProfileRepo
import com.teleferik.ui.more.ProfileViewModel
import com.teleferik.ui.privateTrip.PrivateRepo
import com.teleferik.ui.privateTrip.PrivateTripViewModel

class ViewModelFactory(private val baseRepo: BaseRepo) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> AuthViewModel(baseRepo as AuthRepo) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(baseRepo as ProfileRepo) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(baseRepo as HomeRepo) as T
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(baseRepo as DashboardRepo) as T
            modelClass.isAssignableFrom(PrivateTripViewModel::class.java) -> PrivateTripViewModel(baseRepo as PrivateRepo) as T
//            modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(baseRepo as CartRepo) as T
//            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> CategoriesViewModel(baseRepo as CategoriesRepo) as T
//            modelClass.isAssignableFrom(MoreViewModel::class.java) -> MoreViewModel(baseRepo as MoreRepo) as T
//            modelClass.isAssignableFrom(CategoriesViewModel::class.java) -> CategoriesViewModel(baseRepo as CategoriesRepo) as T
//            modelClass.isAssignableFrom(PaymentViewModel::class.java) -> PaymentViewModel(baseRepo as PaymentRepo) as T
//            modelClass.isAssignableFrom(BranchesViewModel::class.java) -> BranchesViewModel(baseRepo as BranchesRepo) as T
//            modelClass.isAssignableFrom(ProductViewModel::class.java) -> ProductViewModel(baseRepo as ProductRepo) as T
//            modelClass.isAssignableFrom(SearchViewModel::class.java) -> SearchViewModel(baseRepo as SearchRepo) as T
//            modelClass.isAssignableFrom(NotificationsViewModel::class.java) -> NotificationsViewModel(baseRepo as NotificationRepo) as T
            else -> throw IllegalArgumentException("view model class not found ")
        }
    }


}