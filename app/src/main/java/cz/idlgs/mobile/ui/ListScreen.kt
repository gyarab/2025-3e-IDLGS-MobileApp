package cz.idlgs.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import cz.idlgs.mobile.nav.OverviewNavGraph

@Destination<OverviewNavGraph>(start = true)
@Composable
fun ListScreen(modifier: Modifier = Modifier) {
	val mods = remember {
		List(100) {
			val colors = listOf(
				Color.Gray, Color.Red, Color.Yellow, Color.Green, Color.Blue, Color.Magenta
			)
			Pair(
				colors.random(),
				(24..60).random().dp
			)
		}
	}

	LazyVerticalStaggeredGrid(
		columns = StaggeredGridCells.Adaptive(260.dp),
	) {
		items(100) {
			Text(
				"Item ${it + 1}",
				modifier = modifier
					.padding(8.dp)
					.background(mods[it].first, RoundedCornerShape(CornerSize(12.dp)))
					.padding(8.dp)
					.height(mods[it].second),
				style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold),
			)
		}
	}
}