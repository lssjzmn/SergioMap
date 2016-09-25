package com.lssjzmn.sergionavi;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

/**
 * FragmentPagerAdapter 继承自 PagerAdapter。相比通用的 PagerAdapter，该类更专注于每一页均为 Fragment
 * 的情况。如文档所述，该类内的每一个生成的 Fragment 都将保存在内存之中，因此适用于那些相对静态的页，数量也比较少的那种；如果需要处理有很多页
 * ，并且数据动态性较大、占用内存较多的情况，应该使用FragmentStatePagerAdapter。FragmentPagerAdapter
 * 重载实现了几个必须的函数，因此来自 PagerAdapter 的函数，我们只需要实现 getCount()，即可。且，由于
 * FragmentPagerAdapter.instantiateItem() 的实现中，调用了一个新增的虚函数
 * getItem()，因此，我们还至少需要实现一个 getItem()。因此，总体上来说，相对于继承自 PagerAdapter，更方便一些。
 * 
 * •getItem() •该类中新增的一个虚函数。函数的目的为生成新的 Fragment 对象。重载该函数时需要注意这一点。在需要时，该函数将被
 * instantiateItem() 所调用。 •如果需要向 Fragment 对象传递相对静态的数据时，我们一般通过
 * Fragment.setArguments() 来进行，这部分代码应当放到 getItem()。它们只会在新生成 Fragment 对象时执行一遍。
 * •如果需要在生成 Fragment 对象后，将数据集里面一些动态的数据传递给该 Fragment，那么，这部分代码不适合放到 getItem()
 * 中。因为当数据集发生变化时，往往对应的 Fragment 已经生成，如果传递数据部分代码放到了 getItem()
 * 中，这部分代码将不会被调用。这也是为什么很多人发现调用 PagerAdapter.notifyDataSetChanged() 后，getItem()
 * 没有被调用的一个原因。
 * 
 * •instantiateItem() •函数中判断一下要生成的 Fragment 是否已经生成过了，如果生成过了，就使用旧的，旧的将被
 * Fragment.attach()；如果没有，就调用 getItem() 生成一个新的，新的对象将被 FragmentTransation.add()。
 * •FragmentPagerAdapter 会将所有生成的 Fragment 对象通过 FragmentManager 保存起来备用，以后需要该
 * Fragment 时，都会从 FragmentManager 读取，而不会再次调用 getItem() 方法。 •如果需要在生成 Fragment
 * 对象后，将数据集中的一些数据传递给该 Fragment，这部分代码应该放到这个函数的重载里。在我们继承的子类中，重载该函数，并调用
 * FragmentPagerAdapter.instantiateItem() 取得该函数返回 Fragment 对象，然后，我们该 Fragment
 * 对象中对应的方法，将数据传递过去，然后返回该对象。 •否则，如果将这部分传递数据的代码放到 getItem()中，在
 * PagerAdapter.notifyDataSetChanged() 后，这部分数据设置代码将不会被调用。
 * 
 * •destroyItem() •该函数被调用后，会对 Fragment 进行 FragmentTransaction.detach()。这里不是
 * remove()，只是 detach()，因此 Fragment 还在 FragmentManager 管理中，Fragment 所占用的资源不会被释放。
 * 
 * 
 * @author hp
 * 
 */
public class ProposalFragmentPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> fragmentList;
	private final String[] tabNames = new String[] { "音乐", "地点", "发现", "我的" };

	public ProposalFragmentPagerAdapter(FragmentManager fragmentManager,
			List<Fragment> fragmentList) {
		super(fragmentManager);
		this.fragmentList = fragmentList;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		System.out.println("public void destroyItem..." + position);
	}

	// 在viewPager里面
	@Override
	public CharSequence getPageTitle(int position) {
		return tabNames[position];
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		super.setPrimaryItem(container, position, object);
	}

	@Override
	public Fragment getItem(int position) {
		System.out.println("public Fragment getItem..." + position);
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

}
