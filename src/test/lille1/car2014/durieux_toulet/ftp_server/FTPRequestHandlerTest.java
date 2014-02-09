package test.lille1.car2014.durieux_toulet.ftp_server;

import static org.junit.Assert.*;

import lille1.car2014.durieux_toulet.exception.RequestHandlerException;
import lille1.car2014.durieux_toulet.exception.ServerSocketException;
import lille1.car2014.durieux_toulet.exception.SocketException;
import lille1.car2014.durieux_toulet.ftp_server.FTPClient;
import lille1.car2014.durieux_toulet.ftp_server.FTPClientImpl;
import lille1.car2014.durieux_toulet.ftp_server.FTPRequestHandlerImpl;
import lille1.car2014.durieux_toulet.ftp_server.FTPTransfertServerImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FTPRequestHandlerTest {

	@Before
	public void setUp() {
		// requestHandler = new FTPRequestHandlerImpl(new FTPClientImpl(new
		// Socket()));
	}

	@After
	public void tearDown() {
	}

	private int parseMessageCode(String message) {
		String[] splittedMessage = message.split(" ");
		if (splittedMessage.length > 0) {
			return Integer.parseInt(splittedMessage[0]);
		}
		return -1;
	}

	@Test
	public void testRequestUser() {
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(331, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user",
					new FTPClientImpl(), ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestBadPassword() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(430, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS user", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestGoodPassword() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(230, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestTypeNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("TYPE ds", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestInvalidType() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(400, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("TYPE ds", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestValidType() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(200, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("TYPE A", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestValidSecondType() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(200, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("TYPE L N", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestInvalidSecondType() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(400, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("TYPE G N", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestInvalidSecondType2() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(400, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("TYPE E asda", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestListWithUserNotConnected() {
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("LIST -a",
					new FTPClientImpl(), ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestListNotDataConnection() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(443, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("LIST -a", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestList() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.readStringContent();
											transfertServerImpl.close();
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}

									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(226, code);
						}
					}
				});
		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("LIST", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testRequestNLSTWithUserNotConnected() {
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("NLST",
					new FTPClientImpl(), ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestNLSTNotDataConnection() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(443, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("NLST", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestNLST() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.readStringContent();
											transfertServerImpl.close();
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}

									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(226, code);
						}
					}
				});
		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("NLST", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestNLSTNoFolder() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.readStringContent();
											transfertServerImpl.close();
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}
									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(504, code);
						}
					}
				});
		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("NLST /tmp/boom",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSTORWithUserNotConnected() {
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("STOR /tmp/test.txt",
					new FTPClientImpl(), ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestSTORNotDataConnection() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(443, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("STOR /tmp/test.txt",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSTOR() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.writeContent("Test");
											transfertServerImpl.close();
										} catch (RequestHandlerException e) {
											throw new RuntimeException(e);
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}
									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(226, code);
						}
					}
				});

		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("STOR /tmp/text.txt",
					ftpClient, ftpClientSocketMockup).execute();
			FTPRequestHandlerImpl.parseStringRequest("DELE /tmp/text.txt",
					ftpClient, new FTPClientSocketMockup()).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testRequestRETRWithUserNotConnected() {
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("RETR /tmp/test.txt",
					new FTPClientImpl(), ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail();
		}
	}

	@Test
	public void testRequestRETRNotDataConnection() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(443, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("RETR /tmp/test.txt",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestRETRFileDoesNotExist() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl.readContent();
											transfertServerImpl.close();
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}
									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(426, code);
						}
					}
				});

		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("RETR /tmp/text.txt",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestRETR() {
		final FTPClient ftpClient = new FTPClientImpl();

		final FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
		ftpClientSocketMockup
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.readStringContent();
											transfertServerImpl.close();
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}
									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else {
							assertEquals(226, code);
						}
					}
				});

		final FTPClientSocketMockup ftpClientSocketMockupNewFile = new FTPClientSocketMockup();
		ftpClientSocketMockupNewFile
				.addFTPClientSocketListener(new FTPClientSocketListener() {
					@Override
					public void sokcetClosed() {
					}

					@Override
					public void socketOpened() {
					}

					@Override
					public void newWriteMessage(String message) {
						int code = parseMessageCode(message);
						if (code == 150) {
							try {
								final FTPTransfertServerImpl transfertServerImpl = new FTPTransfertServerImpl(
										"127.0.0.1", ftpClient
												.getTransfertServer()
												.getPublicPort());
								new Thread(new Runnable() {

									@Override
									public void run() {
										try {
											transfertServerImpl
													.writeContent("Test");
											transfertServerImpl.close();
										} catch (RequestHandlerException e) {
											throw new RuntimeException(e);
										} catch (SocketException e) {
											throw new RuntimeException(e);
										}
									}
								}).start();
							} catch (ServerSocketException e) {
								throw new RuntimeException(e);
							}
						} else if (code == 226) {
							try {
								FTPRequestHandlerImpl.parseStringRequest(
										"PASV", ftpClient,
										new FTPClientSocketMockup()).execute();
								FTPRequestHandlerImpl.parseStringRequest(
										"RETR /tmp/text.txt", ftpClient,
										ftpClientSocketMockup).execute();

								FTPRequestHandlerImpl.parseStringRequest(
										"DELE /tmp/text.txt", ftpClient,
										new FTPClientSocketMockup()).execute();
							} catch (RequestHandlerException e) {
								fail(e.getMessage());
							}
						}
					}
				});

		try {
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();

			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("STOR /tmp/text.txt",
					ftpClient, ftpClientSocketMockupNewFile).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSystem() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(215, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("SYST", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSystemNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("SYST", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestOptions() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							if (code != 200)
								fail("Options not accpeted");
							assertEquals("test",
									ftpClient.getOptions().get("test"));
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("OPTS test test",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestOptionsNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("OPTS test test",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPWD() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(257, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PWD", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPWDNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("PWD", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestCWD() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(250, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("CWD /", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestCWDFolderNotExist() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(550, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("CWD /blablabla",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestCWDNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("CWD /", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestCDUP() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(250, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("CDUP", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestCDUPNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("CDUP", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMKD() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(226, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"MKD /tmp/testCreationFolder", ftpClient,
					ftpClientSocketMockup).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"DELE /tmp/testCreationFolder", ftpClient,
					new FTPClientSocketMockup()).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMKDFolderNotCreated() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(451, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("MKD /testCreationFolder",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMKDNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("MKD /testCreationFolder",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestRename() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(250, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"MKD /tmp/testCreationFolder", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"RNFR /tmp/testCreationFolder", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"RNTO /tmp/testCreationFolderRN", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"CWD /tmp/testCreationFolderRN", ftpClient,
					ftpClientSocketMockup).execute();
			FTPRequestHandlerImpl.parseStringRequest(
					"RMD /tmp/testCreationFolderRN", ftpClient,
					new FTPClientSocketMockup()).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestRenameNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest(
					"RNFR /testCreationFolder", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMDTM() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(226, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("MDTM /tmp", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMDTMFileNotExist() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(451, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("MDTM /booom", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestMDTMNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("MDTM /tmp", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSIZE() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(226, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("SIZE /tmp", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSIZEFileNotExist() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							assertEquals("226 0", message);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("SIZE /booom", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestSIZENotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("SIZE /tmp", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPORTNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("PORT 127,0,0,1,123,13",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPORTNoServer() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(425, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PORT 127,0,0,1,123,13",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPORTNotValid() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(425, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PORT 127,0,0,125,34",
					ftpClient, ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPASV() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(227, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					ftpClientSocketMockup).execute();
			FTPRequestHandlerImpl.parseStringRequest("ABOR", ftpClient,
					new FTPClientSocketMockup()).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestPASVNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("PASV", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestEPSV() {
		final FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(229, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("USER user", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("PASS pass", ftpClient,
					new FTPClientSocketMockup()).execute();
			FTPRequestHandlerImpl.parseStringRequest("EPSV", ftpClient,
					ftpClientSocketMockup).execute();
			FTPRequestHandlerImpl.parseStringRequest("ABOR", ftpClient,
					new FTPClientSocketMockup()).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testRequestEPSVNotConnected() {
		FTPClient ftpClient = new FTPClientImpl();
		try {
			FTPClientSocketMockup ftpClientSocketMockup = new FTPClientSocketMockup();
			ftpClientSocketMockup
					.addFTPClientSocketListener(new FTPClientSocketListener() {
						@Override
						public void sokcetClosed() {
						}

						@Override
						public void socketOpened() {
						}

						@Override
						public void newWriteMessage(String message) {
							int code = parseMessageCode(message);
							assertEquals(530, code);
						}
					});
			FTPRequestHandlerImpl.parseStringRequest("EPSV", ftpClient,
					ftpClientSocketMockup).execute();
		} catch (RequestHandlerException e) {
			fail(e.getMessage());
		}
	}

}
