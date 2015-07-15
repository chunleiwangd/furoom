package weixin.service.impl;

import org.springframework.stereotype.Service;

import weixin.service.ITestService;

@Service
public class TestServiceImpl implements ITestService {

	@Override
	public String getName() {
		return "wangd";
	}

}
